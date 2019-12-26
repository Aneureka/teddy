package http;

/**
 * @author Aneureka
 * @createdAt 2019-12-17 14:47
 * @description
 **/
public class HttpRequest extends HttpMessage {

    private HttpMethod method;

    private String uri;

    public HttpRequest() {
    }

    public HttpRequest(HttpVersion version, HttpMethod method, String uri, HttpHeaders headers, HttpContent content) {
        super(version, content, headers);
        this.method = method;
        this.uri = uri;
    }

    public HttpMethod method() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String uri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", uri='" + uri + '\'' +
                ", version=" + version +
                ", content=" + content +
                ", headers=" + headers +
                '}';
    }
}
