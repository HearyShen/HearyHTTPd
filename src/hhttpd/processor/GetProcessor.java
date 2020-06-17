package hhttpd.processor;

import hhttpd.resolver.HeaderResolver;
import hhttpd.resolver.RequestLine;
import hhttpd.resolver.RequestLineResolver;
import hhttpd.responser.BinaryResponser;
import hhttpd.responser.NotFoundResponser;
import hhttpd.responser.TextResponser;
import hhttpd.utils.ContentTypeDict;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class GetProcessor {
    public static void handle(String webRoot, InputStream inputStream, OutputStream outputStream) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String firstLine = reader.readLine().strip();
        System.out.println(firstLine);
        RequestLine requestLine = RequestLineResolver.resolve(firstLine);

        boolean isValidGet = requestLine.getMethod().equals("GET")
                && requestLine.getProtocolAndVersion().startsWith("HTTP/1.");

        System.out.println("GetResolver: request is " + (isValidGet ? "valid" : "invalid"));

        Map<String, String> requestHeader = HeaderResolver.resolve(reader);

        File localFile = Path.of(webRoot, requestLine.getPath()).toFile();
        if (localFile.isDirectory()) {
            localFile = Path.of(webRoot, requestLine.getPath(), "index.html").toFile();
        }

        if (localFile.isFile()) {
            String contentType = ContentTypeDict.getContentType(localFile.getName());

            if (contentType.startsWith("text/")) {
                TextResponser.response(localFile, outputStream);
            } else {
                BinaryResponser.response(localFile, outputStream);
            }
        } else {
            NotFoundResponser.response(outputStream);
        }
    }
}
