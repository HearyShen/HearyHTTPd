package hhttpd.utils;

public class ContentTypeDict {
    public static String getContentType(String filename) {
        String contentType = null;
        String suffix = filename.substring(filename.lastIndexOf('.')+1);
        switch (suffix) {
            case "html": contentType = "text/html"; break;
            case "css": contentType = "text/css"; break;
            case "js": contentType = "application/javascript"; break;
            case "jpg": contentType = "image/jpeg"; break;
            case "png": contentType = "image/png"; break;
            case "svg": contentType = "image/svg+xml"; break;
            case "woff2": contentType = "font/woff2"; break;
            default: break;
        }
        return contentType;
    }
}
