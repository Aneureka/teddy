package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Aneureka
 * @createdAt 2019-12-17 16:02
 * @description
 **/
public class ChannelPipeline {

    private ChannelHandlerContext head;

    private ChannelHandlerContext tail;

    private SocketChannel channel;

    public ChannelPipeline() {
        this.channel = null;
    }

    public ChannelPipeline(ChannelHandler... channelHandlers) {
        this();
        for (ChannelHandler handler : channelHandlers) {
            add(handler);
        }
    }

    public void bind(SocketChannel channel) {
        this.channel = channel;
        addFirst(new InitialChannelHandler(channel));
    }

    public boolean bound() {
        return channel != null;
    }

    private void add(ChannelHandler handler) {
        ChannelHandlerContext nextCtx = new ChannelHandlerContext(handler);
        if (head == null) {
            head = nextCtx;
            tail = head;
        } else {
            tail.setNext(nextCtx);
            nextCtx.setPrev(tail);
            tail = nextCtx;
        }
    }

    private void addFirst(ChannelHandler handler) {
        ChannelHandlerContext prevCtx = new ChannelHandlerContext(handler);
        if (head == null) {
            head = prevCtx;
            tail = head;
        } else {
            head.setPrev(prevCtx);
            prevCtx.setNext(head);
            head = prevCtx;
        }
    }

    public void sendUpstream() {
        if (bound()) {
            head.handleUpstream(null);
        }
    }

    public void sendDownStream() {
        if (bound()) {
            tail.handleDownStream(null);
        }
    }
}
