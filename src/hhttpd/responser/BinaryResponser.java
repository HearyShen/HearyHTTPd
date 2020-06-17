package hhttpd.responser;

import hhttpd.utils.ContentTypeDict;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BinaryResponser {
    public static void response(File file, OutputStream outputStream) throws IOException {
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
}
