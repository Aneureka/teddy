package core;

/**
 * @author Aneureka
 * @createdAt 2019-12-17 16:22
 * @description
 **/
public interface ChannelHandler {
    /**
     * handle the message from former handler in the upstream and process.
     *
     * @param ctx
     * @param msg
     */
    default void handleUpStream(ChannelHandlerContext ctx, Object msg) {
        ctx.sendUpstream(msg);
    }

    /**
     * handle the message from latter handler in the downstream and process.
     *
     * @param ctx
     * @param msg
     */
    default void handleDownStream(ChannelHandlerContext ctx, Object msg) {
        ctx.sendDownStream(msg);
    }
}
