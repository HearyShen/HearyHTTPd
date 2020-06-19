package hhttpd.responser;

import hhttpd.utils.ContentTypeDict;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TextResponser {

    public static void response(OutputStream outputStream, File localFile) throws IOException {
        // read text file and build string
        BufferedReader fileReader = new BufferedReader(new FileReader(localFile));
        StringBuilder stringBuilder = new StringBuilder();
        String nextLine = fileReader.readLine();
        while (nextLine != null) {
            stringBuilder.append(nextLine);
            stringBuilder.append("\r\n");
            nextLine = fileReader.readLine();
        }
        String content = stringBuilder.toString();
        int contentLen = content.getBytes(StandardCharsets.UTF_8).length;

        // response to output stream
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write("HTTP/1.0 200 OK\r\n");
        writer.write("Connection: close\r\n");
        writer.write("Content-Type: " + ContentTypeDict.getContentType(localFile.getName()) + "\r\n");
        writer.write("Content-Length: " + contentLen + "\r\n");
        writer.write("\r\n"); // blank line seperates header and content
        writer.write(content);
        writer.flush();

        // close file reader
        fileReader.close();
    }

    public static void response(SocketChannel socketChannel, ByteBuffer byteBuffer, File localFile) throws IOException {
        // read text file and build string
        byte[] bytes = Files.readAllBytes(localFile.toPath());
//        String content = new String(bytes, StandardCharsets.UTF_8);

        String responseHeader = "HTTP/1.0 200 OK\r\n"
                + "Connection: close\r\n"
                + "Content-Type: " + ContentTypeDict.getContentType(localFile.getName()) + "\r\n"
                + "Content-Length: " + bytes.length + "\r\n"
                + "\r\n";

        BytesResponser.response(socketChannel, byteBuffer, responseHeader.getBytes(StandardCharsets.UTF_8));
        BytesResponser.response(socketChannel, byteBuffer, bytes);
    }
}
