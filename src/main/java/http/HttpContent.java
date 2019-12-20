package http;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-20 09:29
 * @description
 **/
public class HttpContent {

    private List<Byte> content;

    public HttpContent() {
    }

    public HttpContent(List<Byte> content) {
        this.content = content;
    }

    public List<Byte> getContent() {
        return content;
    }

    public void setContent(List<Byte> content) {
        this.content = content;
    }
}
