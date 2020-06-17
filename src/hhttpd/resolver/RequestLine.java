package hhttpd.resolver;

import java.util.Map;

public class RequestLine {
    private String method;
    private String path;
    private Map<String, String> querys;
    private String protocolAndVersion;

    public RequestLine(String method, String path, String protocolAndVersion) {
        this.method = method;
        this.path = path;
        this.querys = null;
        this.protocolAndVersion = protocolAndVersion;
    }

    public RequestLine(String method, String path, Map<String, String> querys, String protocolAndVersion) {
        this.method = method;
        this.path = path;
        this.querys = querys;
        this.protocolAndVersion = protocolAndVersion;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocolAndVersion() {
        return protocolAndVersion;
    }

    public void setProtocolAndVersion(String protocolAndVersion) {
        this.protocolAndVersion = protocolAndVersion;
    }

    public Map<String, String> getQuerys() {
        return querys;
    }

    public void setQuerys(Map<String, String> querys) {
        this.querys = querys;
    }
}
