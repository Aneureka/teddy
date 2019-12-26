package http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-20 09:29
 * @description
 **/
public class HttpContent {

    private List<Byte> content;

    public HttpContent() {
        this.content = new ArrayList<>();
    }

    public HttpContent(List<Byte> content) {
        this.content = content;
    }

    public void setContent(String text) {
        this.setContent(text, StandardCharsets.UTF_8);
    }

    public void setContent(String text, Charset charset) {
        byte[] bytes = text.getBytes(charset);
        if (content == null) {
            content = new ArrayList<>();
        } else {
            content.clear();
        }
        for (byte b : bytes) {
            content.add(b);
        }
    }

    public List<Byte> content() {
        return content;
    }

    public int length() {
        if (content == null) {
            return 0;
        }
        return content.size();
    }

    public boolean isEmpty() {
        return length() == 0;
    }

}
