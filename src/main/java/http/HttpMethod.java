package http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aneureka
 * @createdAt  2019-12-07 17:13
 * @description Http request methods
 * @reference https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
 **/
public class HttpMethod {

    /**
     * The GET method requests a representation of the specified resource.
     * Requests using GET should only retrieve data.
     */
    public static final HttpMethod GET = new HttpMethod("GET");
    /**
     * The HEAD method asks for a response identical to that of a GET request, but without the response body.
     */
    public static final HttpMethod HEAD = new HttpMethod("HEAD");
    /**
     * The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server.
     */
    public static final HttpMethod POST = new HttpMethod("POST");
    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     */
    public static final HttpMethod PUT = new HttpMethod("PUT");
    /**
     * The DELETE method deletes the specified resource.
     */
    public static final HttpMethod DELETE = new HttpMethod("DELETE");
    /**
     * The CONNECT method establishes a tunnel to the server identified by the target resource.
     */
    public static final HttpMethod CONNECT = new HttpMethod("CONNECT");
    /**
     * The OPTIONS method is used to describe the communication options for the target resource.
     */
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
    /**
     * The TRACE method performs a message loop-back test along the path to the target resource.
     */
    public static final HttpMethod TRACE = new HttpMethod("TRACE");
    /**
     * The PATCH method is used to apply partial modifications to a resource.
     */
    public static final HttpMethod PATCH = new HttpMethod("PATCH");

    private static final Map<String, HttpMethod> methodMap;

    static {
        methodMap = new HashMap<>();
        methodMap.put("OPTIONS", OPTIONS);
        methodMap.put("GET", GET);
        methodMap.put("HEAD", HEAD);
        methodMap.put("POST", POST);
        methodMap.put("PUT", PUT);
        methodMap.put("PATCH", PATCH);
        methodMap.put("DELETE", DELETE);
        methodMap.put("TRACE", TRACE);
        methodMap.put("CONNECT", CONNECT);
    }

    private final String name;

    public HttpMethod(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }

        if (!methodMap.containsKey(name)) {
            throw new IllegalArgumentException("method not in standard set");
        }

        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static HttpMethod valueOf(String name) {
        return methodMap.get(name);
    }

    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpMethod)) {
            return false;
        }
        HttpMethod that = (HttpMethod) o;
        return name().equals(that.name());
    }

}
