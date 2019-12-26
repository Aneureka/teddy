import core.ChannelHandler;
import core.ChannelHandlerContext;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

import java.util.regex.Matcher;

/**
 * @author Aneureka
 * @createdAt 2019-12-26 11:55
 * @description
 **/
public class HttpServiceHandler implements ChannelHandler {

    @Override
    public void handleUpStream(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            HttpResponse response = new HttpResponse();
            response.setStatus(HttpStatus.OK);
            service(ctx, request, response);
        }
    }

    private void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
        // do nothing
    }
}
