package core;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-17 16:02
 * @description
 **/
public class ChannelPipeline {

    private List<ChannelHandler> channelHandlers;

    public ChannelPipeline() {
        this.channelHandlers = new ArrayList<>();
    }

    public ChannelPipeline(ChannelHandler... channelHandlers) {
        this();
        for (ChannelHandler ch: channelHandlers) {
            addHandler(ch);
        }
    }

    public void addHandler(ChannelHandler handler) {
        this.channelHandlers.add(handler);
    }

    public void handleUpstream(SocketChannel channel) {
        for (int i = 0; i < channelHandlers.size(); i++) {
            if (i == 0) {
                channelHandlers.get(i).read(channel);
            } else {
                channelHandlers.get(i).read(channelHandlers.get(i - 1));
            }
        }
    }

    public void handleDownStream(SocketChannel channel) {
        for (int i = channelHandlers.size() - 1; i >= 0; i--) {
            if (i == 0) {
                channelHandlers.get(i).write(channel);
            } else {
                channelHandlers.get(i).write(channelHandlers.get(i - 1));
            }
        }
    }
}
