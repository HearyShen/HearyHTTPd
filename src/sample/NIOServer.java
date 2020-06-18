package sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/*
[Server] started listening
[Server] selected acceptable socket
[Server] selected readable socket
[Server] starts reading
Client: 'Hello!'
[Server] finished reading
[Server] selected writable socket
[Server] starts writing
[Server] finished writing
[Server] closed writable socket
*/

public class NIOServer {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();

        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.configureBlocking(false);
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        ServerSocket serverSocket = ssChannel.socket();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        serverSocket.bind(address);

        System.out.println("[Server] started listening");

        while (true) {
            // selector blocks until at least one channel is selected
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();

                if (selectionKey.isAcceptable()) {
                    System.out.println("[Server] selected acceptable socket");
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

                    // create a SocketChannel for accepted request socket
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);     // configure as non-blocking

                    // register the socketChannel
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    System.out.println("[Server] selected readable socket");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    System.out.println("[Server] starts reading");
                    System.out.println(readDataFromSocketChannel(socketChannel));
                    System.out.println("[Server] finished reading");

                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                } else if (selectionKey.isWritable()) {
                    System.out.println("[Server] selected writable socket");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    System.out.println("[Server] starts writing");
                    writeDataToSocketChannel(socketChannel);
                    System.out.println("[Server] finished writing");

                    socketChannel.close();
                    System.out.println("[Server] closed writable socket");
                }

                keyIterator.remove();
            }
        }
    }

    private static String readDataFromSocketChannel(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            buffer.clear();
            int n = socketChannel.read(buffer);
            if (n == -1) {
                break;
            }
            buffer.flip();
            int limit = buffer.limit();
            char[] dst = new char[limit];
            for (int i = 0; i < limit; i++) {
                dst[i] = (char) buffer.get(i);
            }
            stringBuilder.append(dst);
            buffer.clear();
        }
        return stringBuilder.toString();
    }

    private static void writeDataToSocketChannel(SocketChannel sChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        String s = "Server: 'Have a nice day!'";
        buffer.put(s.getBytes(StandardCharsets.UTF_8));     // write to buffer
        buffer.flip();          // flip buffer's mode from 'write' to 'read'
        sChannel.write(buffer); // read from buffer, write to channel
        buffer.clear();
    }
}
