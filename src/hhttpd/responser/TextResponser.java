package hhttpd.responser;

import hhttpd.utils.ContentTypeDict;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TextResponser {
    public static void response(File localFile, OutputStream outputStream) throws IOException {
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
}
