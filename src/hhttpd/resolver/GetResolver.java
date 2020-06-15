package hhttpd.resolver;

import hhttpd.utils.ContentTypeDict;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GetResolver {
    public static void resolve(String webRoot, InputStream inputStream, OutputStream outputStream) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        var writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        String firstLine = reader.readLine().strip();
        System.out.println(firstLine);

        String[] firstLineSplits = firstLine.split(" ");
        if (firstLineSplits.length != 3) {
           return;
        }
        String requestMethod = firstLineSplits[0].strip();
        String requestPath = URLDecoder.decode(firstLineSplits[1].strip(), StandardCharsets.UTF_8);
        String requestProtocol = firstLineSplits[2].strip();

        boolean isValidGet = requestMethod.equals("GET") && requestProtocol.startsWith("HTTP/1.");
        System.out.println("GetResolver: request is " + (isValidGet ? "valid" : "invalid"));

        Map<String, String> requestHeader = new HashMap<>();
        while (isValidGet) {
            String headerLine = reader.readLine();
            if (headerLine.isEmpty()) { // HTTP Header reading finishes at blank line
                break;
            }
//            System.out.println(headerLine);
            int colonIndex = headerLine.indexOf(":");
            String key = headerLine.substring(0, colonIndex).strip();
            String value = headerLine.substring(colonIndex+1).strip();
            requestHeader.put(key, value);
        }

        File localFile = Path.of(webRoot, requestPath).toFile();
        if (localFile.isDirectory()) {
            localFile = Path.of(webRoot, requestPath, "index.html").toFile();
        }

        if (localFile.isFile()) {
            System.out.println("GetResolver: local file = " + localFile.getCanonicalPath());
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

            writer.write("HTTP/1.0 200 OK\r\n");
            writer.write("Connection: close\r\n");
            String contentType = ContentTypeDict.getContentType(localFile.getName());
            if (contentType != null) {
                writer.write("Content-Type: " + contentType + "\r\n");
            }
            writer.write("Content-Length: " + contentLen + "\r\n");
            writer.write("\r\n"); // blank line seperates header and content
            writer.write(content);
            writer.flush();

            // close file reader
            fileReader.close();
        } else {
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        }
    }
}
