package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Aneureka
 * @createdAt 2019-12-19 16:34
 * @description
 **/
public class SocketChannelHandler implements ChannelHandler {

    private final static int DEFAULT_BUFFER_SIZE = 1024 * 8;

    private int bufferSize;

    private SocketChannel channel;

    public SocketChannelHandler(SocketChannel channel) {
        this(channel, DEFAULT_BUFFER_SIZE);
    }

    public SocketChannelHandler(SocketChannel channel, int bufferSize) {
        this.channel = channel;
        this.bufferSize = bufferSize;
    }

    @Override
    public void handleUpStream(ChannelHandlerContext ctx, Object msg) {
        if (msg == null) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            int bytesRead = 0;
            try {
                bytesRead = channel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytesRead > 0) {
                ctx.sendUpstream(buffer);
            } else if (bytesRead == -1) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ctx.sendUpstream(msg);
        }
    }

    @Override
    public void handleDownStream(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuffer) {
            ByteBuffer buffer = (ByteBuffer) msg;
            while (buffer.hasRemaining()) {
                try {
                    channel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            ctx.sendDownStream(msg);
        }
    }
}
