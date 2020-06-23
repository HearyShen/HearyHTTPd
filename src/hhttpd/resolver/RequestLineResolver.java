package hhttpd.resolver;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RequestLineResolver {
    public static RequestLine resolve(String requestLine) throws URISyntaxException {
        String[] firstLineSplits = requestLine.split(" ");
        if (firstLineSplits.length != 3) {
            throw new IllegalArgumentException("Invalid request line: '" + requestLine + "'");
        }

        // resolve request method
        String requestMethod = firstLineSplits[0].strip();

        // resolve request URL
        String requestUriString = URLDecoder.decode(firstLineSplits[1].strip(), StandardCharsets.UTF_8);
        URI requestUri = new URI(requestUriString);

        // resolve request protocol (and version)
        String requestProtocol = firstLineSplits[2].strip();

        return new RequestLine(requestMethod, requestUri, requestProtocol);
    }
}
