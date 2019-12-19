package core;

/**
 * @author Aneureka
 * @createdAt 2019-12-19 11:34
 * @description
 **/
public class ChannelHandlerContext {

    private ChannelHandler handler;

    private ChannelHandlerContext prev;

    private ChannelHandlerContext next;

    public ChannelHandlerContext(ChannelHandler handler) {
        this.handler = handler;
        this.prev = null;
        this.next = null;
    }

    public void handleUpstream(Object msg) {
        handler.handleUpStream(this, msg);
    }

    public void handleDownStream(Object msg) {
        handler.handleDownStream(this, msg);
    }

    public void sendUpstream(Object msg) {
        if (next != null) {
            next.handleUpstream(msg);
        }
    }

    public void sendDownStream(Object msg) {
        if (prev != null) {
            prev.handleDownStream(msg);
        }
    }

    public ChannelHandler getHandler() {
        return handler;
    }

    public void setHandler(ChannelHandler handler) {
        this.handler = handler;
    }

    public ChannelHandlerContext getPrev() {
        return prev;
    }

    public void setPrev(ChannelHandlerContext prev) {
        this.prev = prev;
    }

    public ChannelHandlerContext getNext() {
        return next;
    }

    public void setNext(ChannelHandlerContext next) {
        this.next = next;
    }
}
