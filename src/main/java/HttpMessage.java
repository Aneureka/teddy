import tcp.ChannelBuffer;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 17:20
 * @description
 **/
public interface HttpMessage {

    String getVersion();

    void setVersion(String version);

    String getHeader(String name);

    List<String> getHeaders(String name);

    Map<String, String> getHeaders();

    boolean containsHeader(String name);

    Set<String> getHeaderNames();

    void addHeader(String name, Object value);

    void setHeader(String name, Object value);

    void removeHeader(String name);

    void clearHeaders();

    boolean isChunked();

    void setChunked(boolean chunked);

    ChannelBuffer getContent();

    void setContent(ChannelBuffer content);

}
