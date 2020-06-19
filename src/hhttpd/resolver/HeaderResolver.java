package hhttpd.resolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeaderResolver {
    public static Map<String, String> resolve(BufferedReader reader) throws IOException {
        Map<String, String> resolvedHeader = new HashMap<>();
        while (true) {
            String headerLine = reader.readLine();
            if (headerLine.isEmpty()) { // HTTP Header reading finishes at blank line
                break;
            }
            int colonIndex = headerLine.indexOf(":");
            String key = headerLine.substring(0, colonIndex).strip();
            String value = headerLine.substring(colonIndex+1).strip();
            resolvedHeader.put(key, value);
        }
        return resolvedHeader;
    }
}
