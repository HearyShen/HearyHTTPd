package hhttpd.responser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class NotFoundResponser {
    public static void response(OutputStream outputStream) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write("HTTP/1.0 404 Not Found\r\n");
        writer.write("Content-Length: 0\r\n");
        writer.write("\r\n");
        writer.flush();
    }
}
