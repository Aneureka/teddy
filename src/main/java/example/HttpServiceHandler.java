package example;

import core.ChannelHandler;
import core.ChannelHandlerContext;
import http.HttpRequest;
import http.HttpResponse;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Aneureka
 * @createdAt 2019-12-26 11:55
 * @description
 **/
public class HttpServiceHandler implements ChannelHandler {

    Queue<HttpResponse> responseQueue;

    public HttpServiceHandler() {
        responseQueue = new LinkedList<>();
    }

    @Override
    public void handleUpStream(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            HttpResponse response = new HttpResponse();
            service(ctx, request, response);
            responseQueue.add(response);
        }
    }

    @Override
    public void handleDownStream(ChannelHandlerContext ctx, Object msg) {
        if (msg == null) {
            while (!responseQueue.isEmpty()) {
                ctx.sendDownStream(responseQueue.poll());
            }
        } else {
            ctx.sendDownStream(msg);
        }
    }

    private void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
        response.setContent("Hello, teddy!");
    }
}
