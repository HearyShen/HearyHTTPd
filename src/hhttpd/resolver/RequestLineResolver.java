package hhttpd.resolver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestLineResolver {
    public static RequestLine resolve(String requestLine) {
        String[] firstLineSplits = requestLine.split(" ");
        if (firstLineSplits.length != 3) {
            throw new IllegalArgumentException("Invalid request line: '" + requestLine + "'");
        }

        // resolve request method
        String requestMethod = firstLineSplits[0].strip();

        // resolve request URL
        String requestURL = URLDecoder.decode(firstLineSplits[1].strip(), StandardCharsets.UTF_8);
        String requestPath = requestURL;
        Map<String, String> requestQueryKVs = null;
        int queryStart = requestURL.indexOf("?");
        if (queryStart >= 0) {
            // has query string in GET
            requestPath = requestURL.substring(0, queryStart);
            String requestQueryString = requestURL.substring(queryStart+1);

            if (! requestQueryString.isEmpty()) {
                // resolve query key-value
                requestQueryKVs = new HashMap<>();
                for (String eachQueryString : requestQueryString.split("&")) {
                    int equalSignIndex = eachQueryString.indexOf("=");
                    String queryKey = eachQueryString;
                    String queryValue = "";
                    if (equalSignIndex >= 0) {
                        queryKey = eachQueryString.substring(0, equalSignIndex);
                        queryValue = eachQueryString.substring(equalSignIndex+1);
                    }
                    requestQueryKVs.put(queryKey, queryValue);
                }
            }
        }

        // resolve request protocol (and version)
        String requestProtocol = firstLineSplits[2].strip();

        return new RequestLine(requestMethod, requestPath, requestQueryKVs, requestProtocol);
    }
}
