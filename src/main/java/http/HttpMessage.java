package http;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 17:20
 * @description
 **/
public class HttpMessage {

    private HttpVersion version;

    private HttpContent content;

    private HttpHeaders headers;

    public HttpMessage() {
    }

    public HttpMessage(HttpVersion version, HttpContent content, HttpHeaders headers) {
        this.version = version;
        this.content = content;
        this.headers = headers;
    }

    public HttpVersion version() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public HttpContent content() {
        return content;
    }

    public void setContent(HttpContent content) {
        this.content = content;
    }

    public HttpHeaders headers() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }
}
