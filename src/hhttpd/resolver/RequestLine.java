package hhttpd.resolver;

import java.net.URI;
import java.net.URISyntaxException;

public class RequestLine {
    private String method;
    private URI uri;
    private String protocolAndVersion;

    public RequestLine(String method, URI uri, String protocolAndVersion) {
        this.method = method;
        this.uri = uri;
        this.protocolAndVersion = protocolAndVersion;
    }

    public RequestLine(String method, String uriString, String protocolAndVersion) throws URISyntaxException {
        this(method, new URI(uriString), protocolAndVersion);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getProtocolAndVersion() {
        return protocolAndVersion;
    }

    public void setProtocolAndVersion(String protocolAndVersion) {
        this.protocolAndVersion = protocolAndVersion;
    }


}
