package http;

import util.CodecUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-20 09:36
 * @description
 **/
public class HttpRequestDecoder {

    private State curState = State.READ_INITIAL;

    private HttpRequest curRequest = new HttpRequest();

    private int curChunkLength = -1;

    private List<Byte> curChunk = new ArrayList<>();

    public boolean decode(List<Byte> in, List<HttpRequest> requests) {
        int limit = in.size();
        int start = 0;
        while (start != limit) {
            switch (curState) {
                case READ_INITIAL: {
                    int nextStart = decodeInitial(start, in);
                    if (nextStart == -1) {
                        removeUsedContent(start, in);
                        return false;
                    }
                    start = nextStart;
                    curState = State.READ_HEADER;
                    break;
                }
                case READ_HEADER: {
                    int nextStart = decodeHeaders(start, in);
                    if (nextStart == -1) {
                        removeUsedContent(start, in);
                        return false;
                    }
                    start = nextStart;
                    if (HttpHeaders.hasContentLength(this.curRequest)) {
                        // contentLength is 0 (like HEAD) and just add the request in list
                        if (HttpHeaders.getContentLength(this.curRequest) == 0) {
                            requests.add(this.curRequest);
                            reset();
                        } else {
                            curState = State.READ_FIXED_LENGTH_CONTENT;
                        }
                    } else if (HttpHeaders.isChunkTransfer(this.curRequest)) {
                        curState = State.READ_CHUNK_SIZE;
                    } else {
                        // if a request does not have either a transfer-encoding or a content-length header,
                        // then the message body length is 0
                        // reference: https://tools.ietf.org/html/rfc7230#section-3.3.3
                        requests.add(this.curRequest);
                        reset();
                    }
                    break;
                }
                case READ_FIXED_LENGTH_CONTENT: {
                    int nextStart = decodeContent(start, in);
                    if (nextStart == -1) {
                        removeUsedContent(start, in);
                        return false;
                    }
                    start = nextStart;
                    requests.add(this.curRequest);
                    reset();
                    break;
                }
                case READ_CHUNK_SIZE: {
                    int result = decodeChunkSize(start, in);
                    if (result == -1) {
                        removeUsedContent(start, in);
                        return false;
                    }
                    start = result;
                    if (this.curChunkLength != 0) {
                        curState = State.READ_CHUNKED_CONTENT;
                    } else {
                        curState = State.READ_CHUNK_FOOTER;
                    }
                    break;
                }
                case READ_CHUNKED_CONTENT: {
                    int result = decodeChunkContent(start, in);
                    if (result == -1) {
                        removeUsedContent(start, in);
                        return false;
                    }
                    start = result;
                    curState = State.READ_CHUNK_SIZE;
                    break;
                }
                case READ_CHUNK_FOOTER: {
                    int result = decodeChunkFooter(start, in, this.curRequest);
                    if (result == -1) {
                        removeUsedContent(start, in);
                        return false;
                    }
                    start = result;
                    requests.add(this.curRequest);
                    reset();
                    break;
                }
            }
        }
        removeUsedContent(start, in);
        return true;
    }

    private int decodeInitial(int start, List<Byte> in) {
        List<Byte> content = new ArrayList<>();
        int index = start;
        int limit = in.size();
        byte cur;
        while ((cur = in.get(index)) != '\r') {
            content.add(cur);
            index++;
            // fail to read initial line
            if (index == limit) {
                return -1;
            }
        }

        // read \n
        index++;
        // read next line
        index++;

        // trim to remove control characters or whitespace ahead
        String[] initialItems = CodecUtil.bytesToString(content).trim().split("\\s");
        // invalid initial line
        if (initialItems.length != 3) {
            return -1;
        }
        // set method
        this.curRequest.setMethod(HttpMethod.valueOf(initialItems[0]));
        // set uri
        this.curRequest.setUri(initialItems[1]);
        // set version
        this.curRequest.setVersion(HttpVersion.valueOf(initialItems[2]));

        return index;
    }

    private int decodeHeaders(int start, List<Byte> in) {
        HttpHeaders httpHeaders = new HttpHeaders();

        int limit = in.size();
        int index = start;
        int countEnd = 0;
        while (countEnd != 2) {
            if (index >= limit) {
                return -1;
            }

            List<Byte> content = new ArrayList<>();
            byte cur;
            while ((cur = in.get(index)) != '\r') {
                countEnd = 0;
                content.add(cur);
                index++;
                if (index == limit) {
                    return -1;
                }
            }

            // read '\n'
            index++;
            // read next line
            index++;

            // read two continuous \r\n
            if (countEnd == 1) {
                break;
            }
            String[] header = CodecUtil.bytesToString(content).split(":");
            httpHeaders.set(header[0].trim(), header[1].trim());
            countEnd++;
        }

        this.curRequest.setHeaders(httpHeaders);
        return index;
    }

    private int decodeContent(int start, List<Byte> in) {
        List<Byte> content = new ArrayList<>();
        int limit = in.size();
        int contentLength = HttpHeaders.getContentLength(this.curRequest);
        for (int i = 0; i < contentLength; i++) {
            if (start + i < limit) {
                content.add(in.get(start + i));
            } else {
                return -1;
            }
        }
        this.curRequest.setContent(new HttpContent(content));
        return start + contentLength;
    }

    private int decodeChunkSize(int start, List<Byte> in) {
        List<Byte> content = new ArrayList<>();
        int limit = in.size();
        int index = start;
        byte cur;
        while ((cur = in.get(index)) != '\r') {
            content.add(cur);
            index++;
            if (index == limit) {
                return -1;
            }
        }

        // read \n
        index++;
        // read next line
        index++;
        this.curChunkLength = CodecUtil.bytesToInteger(content);
        return index;
    }

    private int decodeChunkContent(int start, List<Byte> in) {
        List<Byte> content = new ArrayList<>();
        int limit = in.size();
        for (int i = 0; i < this.curChunkLength; i++) {
            if (start + i < limit) {
                content.add(in.get(start + i));
            } else {
                return -1;
            }
        }

        int index = start + this.curChunkLength;
        // incomplete or wrong \r\n
        if (limit - index < 2 || in.get(index++) != '\r' || in.get(index++) != '\n') {
            return -1;
        }
        index++;
        // completely read chunk content
        this.curChunk.addAll(content);
        return index;
    }

    private int decodeChunkFooter(int start, List<Byte> in, HttpRequest request) {

        int limit = in.size();
        int index = start;
        // incomplete or wrong \r\n
        if (limit - index < 2 || in.get(index++) != '\r' || in.get(index++) != '\n') {
            return -1;
        }
        index++;
        // add all chunk content to request message
        request.setContent(new HttpContent(this.curChunk));
        return index;
    }

    private void reset() {
        this.curState = State.READ_INITIAL;
        this.curRequest = new HttpRequest();
        this.curChunk = new ArrayList<>();
        this.curChunkLength = -1;
    }

    private void removeUsedContent(int start, List<Byte> in) {
        if (start > 0) {
            in.subList(0, start).clear();
        }
    }

    private enum State {
        READ_INITIAL,
        READ_HEADER,
        READ_FIXED_LENGTH_CONTENT,
        READ_CHUNK_SIZE,
        READ_CHUNKED_CONTENT,
        READ_CHUNK_FOOTER,
    }

}
