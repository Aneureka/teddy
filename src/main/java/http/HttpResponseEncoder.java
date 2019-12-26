package http;

import util.CodecUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Aneureka
 * @createdAt 2019-12-20 10:30
 * @description
 **/
public class HttpResponseEncoder {

    private State curState = State.ENCODE_INITIAL;

    public void encode(Object message, List<Byte> target) {
        List<Byte> curContent = new ArrayList<>();
        boolean isEnd = false;
        while (!isEnd) {
            switch (curState) {
                case ENCODE_INITIAL: {
                    if (message instanceof HttpResponse) {
                        encodeInitial((HttpResponse) message, curContent);
                        curState = State.ENCODE_HEADER;
                    } else {
                        throw new IllegalArgumentException("cannot encode object: " + message);
                    }
                }
                case ENCODE_HEADER: {
                    HttpResponse response = (HttpResponse) message;
                    encodeHeaders(response, curContent);

                    if (response.headers().containsContentLength()) {
                        if (response.headers().get(HttpHeaders.Names.CONTENT_LENGTH).equals("0")) {
                            target.addAll(curContent);
                            reset();
                            curContent.clear();
                            isEnd = true;
                        } else {
                            curState = State.ENCODE_FIXED_LENGTH_CONTENT;
                        }
                    } else if (response.headers().isChunkTransfer()) {
                        curState = State.ENCODE_VARIABLE_LENGTH_CONTENT;
                    } else {
                        target.addAll(curContent);
                        reset();
                        curContent.clear();
                        isEnd = true;
                    }
                    break;
                }
                case ENCODE_FIXED_LENGTH_CONTENT: {
                    encodeContent((HttpResponse) message, curContent);
                    target.addAll(curContent);
                    reset();
                    curContent.clear();
                    isEnd = true;
                    break;
                }
                case ENCODE_VARIABLE_LENGTH_CONTENT: {
                    target.addAll(curContent);
                    curContent.clear();
                    curState = State.ENCODE_WAIT_CONTENT;

                    // this response may contain the first chunk
                    HttpResponse response = (HttpResponse) message;
                    if (response.content() != null && !response.content().isEmpty()) {
                        encodeChunkContent(response.content(), curContent);
                        target.addAll(curContent);
                        curContent.clear();
                    }
                    isEnd = true;
                    break;
                }
                case ENCODE_WAIT_CONTENT: {
                    if (message instanceof HttpContent) {
                        HttpContent content = (HttpContent) message;
                        encodeChunkContent(content, curContent);
                        target.addAll(curContent);
                        curContent.clear();
                        // the last chunk
                        if (content.isEmpty()) {
                            reset();
                        }
                    } else {
                        throw new IllegalArgumentException("invalid http content");
                    }

                    isEnd = true;
                    break;
                }
            }
        }
    }

    private void encodeInitial(HttpResponse response, List<Byte> curContent) {
        StringBuilder sb = new StringBuilder();
        sb.append(response.version().text())
                .append(" ")
                .append(response.status().code())
                .append(" ")
                .append(response.status().reasonPhrase())
                .append("\r\n");
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        addContent(bytes, curContent);
    }

    private void encodeHeaders(HttpResponse response, List<Byte> curContent) {
        HttpHeaders httpHeaders = response.headers();
        Iterator<Map.Entry<String, String>> iterator = httpHeaders.headersIterator();
        StringBuilder sb = new StringBuilder();

        //迭代获取所有首部
        while (iterator.hasNext()) {
            Map.Entry<String, String> header = iterator.next();
            sb.append(header.getKey())
                    .append(":")
                    .append(header.getValue())
                    .append("\r\n");
        }
        sb.append("\r\n");

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        addContent(bytes, curContent);
    }

    private void encodeContent(HttpResponse response, List<Byte> curContent) {
        if (!response.content().isEmpty()) {
            List<Byte> content = response.content().content();
            addContent(content, curContent);
        }
    }

    private void encodeChunkContent(HttpContent httpContent, List<Byte> curContent) {
        List<Byte> content = httpContent.content();
        // size
        this.addContent(CodecUtil.integerToBytes(content.size()), curContent);
        // \r\n
        this.addSeparator(curContent);
        // content
        this.addContent(content, curContent);
        // \r\n
        this.addSeparator(curContent);
    }

    private void addContent(byte[] message, List<Byte> curContent) {
        for (byte e : message) {
            curContent.add(e);
        }
    }

    private void addContent(List<Byte> message, List<Byte> curContent) {
        curContent.addAll(message);
    }

    private void addContent(ByteBuffer buffer, List<Byte> curContent) {
        if (buffer.hasArray()) {
            addContent(buffer.array(), curContent);
        } else {
            int limit = buffer.limit();
            for (int i = 0; i < limit; i++) {
                curContent.add(buffer.get(i));
            }
        }
    }

    private void addSeparator(List<Byte> curContent) {
        byte[] separator = "\r\n".getBytes(StandardCharsets.UTF_8);
        for (byte e : separator) {
            curContent.add(e);
        }
    }

    private ByteBuffer getBuffer(List<Byte> curContent) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(curContent.size());
        for (Byte b : curContent) {
            byteBuffer.put(b);
        }
        return byteBuffer;
    }

    private void reset() {
        curState = State.ENCODE_INITIAL;
    }

    private enum State {
        ENCODE_INITIAL,
        ENCODE_HEADER,
        ENCODE_VARIABLE_LENGTH_CONTENT,
        ENCODE_FIXED_LENGTH_CONTENT,
        ENCODE_WAIT_CONTENT
    }
}
