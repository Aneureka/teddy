import java.util.Map;

/**
 * @author Aneureka
 * @createdAt 2019-12-07 17:12
 * @description Http request model
 **/
public class Request {

    private HttpMethod method;

    private String uri;

    private String version;

    private Map<String, String> headers;

    private String message;

}
