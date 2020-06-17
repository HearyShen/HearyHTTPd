package hhttpd.resolver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RequestLineResolver {
    public static RequestLine resolve(String requestLine) {
        String[] firstLineSplits = requestLine.split(" ");
        if (firstLineSplits.length != 3) {
            throw new IllegalArgumentException("Invalid request line: '" + requestLine + "'");
        }
        String requestMethod = firstLineSplits[0].strip();
        String requestPath = URLDecoder.decode(firstLineSplits[1].strip(), StandardCharsets.UTF_8);
        String requestProtocol = firstLineSplits[2].strip();
        return new RequestLine(requestMethod, requestPath, requestProtocol);
    }
}
