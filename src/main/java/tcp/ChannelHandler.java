package tcp;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 17:15
 * @description
 **/
public class ChannelHandler {

    private Protocol protocol;

    public ChannelHandler(Protocol protocol) {
        this.protocol = protocol;
    }

    void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ChannelBuffer channelBuffer = (ChannelBuffer) key.attachment();
        try {
            channelBuffer.readFromChannel(socketChannel);
            key.interestOps(SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    void handleWrite(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ChannelBuffer channelBuffer = (ChannelBuffer) key.attachment();
        channelBuffer.writeToChannel(socketChannel, this.protocol);
        key.interestOps(SelectionKey.OP_READ);
    }

}
