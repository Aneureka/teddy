import java.nio.ByteBuffer;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 17:20
 * @description
 **/
public class HttpMessage {

    private String httpVersion;

    private ByteBuffer content;

    private HttpHeaders httpHeaders;

    public HttpMessage() {
    }

    public HttpMessage(String httpVersion, ByteBuffer content, HttpHeaders httpHeaders) {
        this.httpVersion = httpVersion;
        this.content = content;
        this.httpHeaders = httpHeaders;
    }

    public String version() {
        return httpVersion;
    }

    public void setVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public ByteBuffer content() {
        return content;
    }

    public void setContent(ByteBuffer content) {
        this.content = content;
    }

    public HttpHeaders headers() {
        return httpHeaders;
    }

    public void setHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
