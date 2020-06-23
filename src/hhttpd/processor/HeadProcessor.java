package hhttpd.processor;

import hhttpd.resolver.RequestLine;
import hhttpd.responser.NotFoundResponser;
import hhttpd.utils.ContentTypeDict;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class HeadProcessor {
    public static void handle(String webRoot,
                              SocketChannel socketChannel,
                              ByteBuffer byteBuffer,
                              String message,
                              RequestLine requestLine) throws IOException {
        File localFile = Path.of(webRoot, requestLine.getUri().getPath()).toFile();

        if (localFile.isDirectory()) {
            // request index.html as default
            localFile = Path.of(webRoot, requestLine.getUri().getPath(), "index.html").toFile();
        }

        if (localFile.isFile()) {
            String contentType = ContentTypeDict.getContentType(localFile.getName());
            // if file exists
        } else {
            // if file not exists
            NotFoundResponser.response(socketChannel, byteBuffer);
        }
    }
}
