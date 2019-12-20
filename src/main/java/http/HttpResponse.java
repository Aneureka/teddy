package http;

/**
 * @author Aneureka
 * @createdAt 2019-12-20 09:37
 * @description
 **/
public class HttpResponse extends HttpMessage {

    private HttpStatus status;

    public HttpResponse() {
        this(new HttpContent(), HttpStatus.OK, HttpVersion.HTTP_1_1, HttpHeaders.defaultHeaders);
    }

    public HttpResponse(HttpContent content) {
        this(content, HttpStatus.OK, HttpVersion.HTTP_1_1, HttpHeaders.defaultHeaders);
    }

    public HttpResponse(HttpContent content, HttpStatus status, HttpVersion version, HttpHeaders headers) {
        super(version, content, headers);
        this.status = status;
    }
}