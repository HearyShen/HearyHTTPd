package hhttpd.responser;

import hhttpd.utils.ContentTypeDict;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class BinaryResponser {

    public static void response(OutputStream outputStream, File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);

        outputStream.write("HTTP/1.0 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write("Connection: close\r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Type: " + ContentTypeDict.getContentType(file.getName()) + "\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.write(("Content-Length: " + file.length() + "\r\n").getBytes(StandardCharsets.UTF_8));
        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8)); // blank line seperates header and content

        byte[] buffer = new byte[2048];
        while (true) {
            int len = fileInputStream.read(buffer, 0, 2048);
            if (len < 0) {
                break;
            }
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        fileInputStream.close();
    }

    public static void response(SocketChannel socketChannel, ByteBuffer byteBuffer, File localFile) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(localFile.toPath())) {
            // response header
            String responseHeader = "HTTP/1.0 200 OK\r\n"
                    + "Connection: close\r\n"
                    + "Content-Type: " + ContentTypeDict.getContentType(localFile.getName()) + "\r\n"
                    + "Content-Length: " + fileChannel.size() + "\r\n"
                    + "\r\n";

            BytesResponser.response(socketChannel, byteBuffer, responseHeader.getBytes(StandardCharsets.UTF_8));

            // response content
//            long tic = System.currentTimeMillis();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            socketChannel.write(mappedByteBuffer);
//            long toc = System.currentTimeMillis();
//            System.out.println("response " + localFile.getCanonicalPath() + " in " + (toc-tic) + " ms");
        }
    }
}
