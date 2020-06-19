package hhttpd.responser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ForbiddenResponser {

    public static void response(OutputStream outputStream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write("HTTP/1.0 403 Forbidden\r\n");
        writer.write("Content-Length: 0\r\n");
        writer.write("\r\n");
        writer.flush();
    }

    public static void response(SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException {
        String message = "HTTP/1.0 403 Forbidden\r\n" + "Content-Length: 0\r\n" + "\r\n";
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

        BytesResponser.response(socketChannel, byteBuffer, bytes);
    }
}
