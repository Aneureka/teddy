import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 17:26
 * @description
 **/
public class HttpHeaders {

    /**
     * Standard HTTP header names.
     */
    public static final class Names {
        public static final String ACCEPT = "Accept";
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        // ...
    }

    /**
     * Standard HTTP header values.
     */
    public static final class Values {
        public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String BASE64 = "base64";
        // ...
    }
}
