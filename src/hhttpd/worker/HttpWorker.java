package hhttpd.worker;

import hhttpd.processor.GetProcessor;
import hhttpd.processor.HeadProcessor;
import hhttpd.resolver.RequestLine;
import hhttpd.resolver.RequestLineResolver;
import hhttpd.responser.ForbiddenResponser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class HttpWorker implements Runnable{

    private static final int defaultByteBufferSize = 8192;

    private final String webRoot;
    private final SocketChannel socketChannel;
    private final ByteBuffer byteBuffer;

    public HttpWorker(String webRoot, SocketChannel socketChannel) {
        this.webRoot = webRoot;
        this.socketChannel = socketChannel;
        this.byteBuffer = ByteBuffer.allocateDirect(defaultByteBufferSize);
    }

    @Override
    public void run() {
        long tic = System.currentTimeMillis();
//        try {
//            System.out.println("HttpWorker [" + this.id + "]: starts working on socketChannel from " + socketChannel.getRemoteAddress());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // read the complete request message from NIO SocketChannel
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            try {
                byteBuffer.clear();
                int ret = socketChannel.read(byteBuffer);   // write to buffer
                if (ret == -1 || ret == 0) {
                    break;
                }
                byteBuffer.flip();      // read from buffer
                byte[] bytes = new byte[byteBuffer.limit()];
                byteBuffer.get(bytes);
                String strInBuffer = new String(bytes, StandardCharsets.UTF_8);
                stringBuilder.append(strInBuffer);
                byteBuffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
//        // close TCP input after reading
//        try {
//            socketChannel.shutdownInput();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // get request message
        String requestMessage = stringBuilder.toString();
//        try {
//            System.out.println("HttpWorker [" + this.id + "]: finished reading requests from " + socketChannel.getRemoteAddress());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // resolve request line and dispatch to corresponding processor
        int requestLineEnd = requestMessage.indexOf("\r\n");
        RequestLine requestLine = RequestLineResolver.resolve(requestMessage.substring(0, requestLineEnd));
        try {
            if (requestLine.getMethod().equals("GET") && requestLine.getProtocolAndVersion().startsWith("HTTP/1.")) {
                // HTTP GET
                GetProcessor.handle(webRoot, socketChannel, byteBuffer, requestMessage, requestLine);
            } else if (requestLine.getMethod().equals("HEAD")) {
                // HTTP HEAD
                HeadProcessor.handle(webRoot, socketChannel, byteBuffer, requestMessage, requestLine);
            } else {
                // not supported
                ForbiddenResponser.response(socketChannel, byteBuffer);
            }

            long toc = System.currentTimeMillis();
            // status
            System.out.println("HttpWorker: completed "
                    + requestLine.getMethod() + " " + requestLine.getPath() + " " + requestLine.getProtocolAndVersion()
                    + " from " + socketChannel.getRemoteAddress()
                    + " in " + (toc-tic) + " ms, " + toc
            );

            // close socket channel after responding
//            System.out.println("HttpWorker [" + this.id + "]: finished responsing requests from " + socketChannel.getRemoteAddress());
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
