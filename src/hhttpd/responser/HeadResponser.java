package hhttpd.responser;

import hhttpd.utils.ContentTypeDict;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class HeadResponser {
    public static void response(SocketChannel socketChannel, ByteBuffer byteBuffer, File localFile) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(localFile.toPath())) {
            // response header
            String responseHeader = "HTTP/1.0 200 OK\r\n"
                    + "Connection: close\r\n"
                    + "Content-Type: " + ContentTypeDict.getContentType(localFile.getName()) + "\r\n"
                    + "Content-Length: " + fileChannel.size() + "\r\n"
                    + "\r\n";

            BytesResponser.response(socketChannel, byteBuffer, responseHeader.getBytes(StandardCharsets.UTF_8));
            // no content data in head response
        }
    }
}
