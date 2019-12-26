package http;

import core.ByteToMessageCodec;

import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-19 11:10
 * @description
 **/
public class HttpMessageCodec extends ByteToMessageCodec<HttpRequest> {

    private HttpRequestDecoder decoder;

    private HttpResponseEncoder encoder;

    public HttpMessageCodec() {
        this(new HttpRequestDecoder(), new HttpResponseEncoder());
    }

    public HttpMessageCodec(HttpRequestDecoder decoder, HttpResponseEncoder encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    protected boolean decode(List<Byte> in, List<HttpRequest> out) {
        int size = out.size();
        boolean res = decoder.decode(in, out);
        return res && size < out.size();
    }

    @Override
    protected boolean encode(Object in, List<Byte> out) {
        if (in instanceof HttpResponse || in instanceof HttpContent) {
            int size = out.size();
            encoder.encode(in, out);
            return size < out.size();
        } else {
            return false;
        }
    }
}
