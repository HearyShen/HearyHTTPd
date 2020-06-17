package hhttpd.resolver;

public class RequestLine {
    private String method;
    private String path;
    private String protocolAndVersion;

    public RequestLine(String method, String path, String protocolAndVersion) {
        this.method = method;
        this.path = path;
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
}
