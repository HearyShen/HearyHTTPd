package hhttpd.utils;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeDict {

    private static final Map<String, String> contentTypeDict;
    static {
        contentTypeDict = new HashMap<>();
        contentTypeDict.put(".bmp", "application/x-bmp");
        contentTypeDict.put(".css", "text/css");
        contentTypeDict.put(".doc", "application/msword");
        contentTypeDict.put(".gif", "image/gif");
        contentTypeDict.put(".htm", "text/html");
        contentTypeDict.put(".html", "text/html");
        contentTypeDict.put("ico", "image/x-icon");
        contentTypeDict.put(".img", "application/x-img");
        contentTypeDict.put(".jpe", "image/jpeg");
        contentTypeDict.put(".jpeg", "image/jpeg");
        contentTypeDict.put(".jpg", "image/jpeg");
        contentTypeDict.put(".js", "application/x-javascript");
        contentTypeDict.put(".pdf", "application/pdf");
        contentTypeDict.put(".png", "image/png");
        contentTypeDict.put(".svg", "text/xml");
        contentTypeDict.put(".tif", "image/tiff");
        contentTypeDict.put(".tiff", "image/tiff");
        contentTypeDict.put(".txt", "text/plain");
        contentTypeDict.put(".xhtml", "text/html");
        contentTypeDict.put(".xml", "text/xml");
        contentTypeDict.put(".woff", "font/woff");
        contentTypeDict.put(".woff2", "font/woff2");
    }
    public static final String defaultContentType = "application/octet-stream";

    public static String getContentType(String filename) {
        String suffix = filename.substring(filename.lastIndexOf('.')).toLowerCase();
        return contentTypeDict.getOrDefault(suffix, defaultContentType);
    }
}
