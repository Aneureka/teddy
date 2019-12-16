import tcp.ChannelBuffer;
import tcp.ChannelHandler;
import tcp.Protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 19:31
 * @description
 **/
public class HttpProtocol implements Protocol {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public HttpProtocol() {}

    @Override
    public void process(ChannelBuffer channelBuffer) {
        ByteBuffer buffer = ByteBuffer.allocate(ChannelBuffer.BUFFER_SIZE);
        channelBuffer.pullFromReadBuffer(buffer);
        channelBuffer.addToWriteBuffer(buffer, buffer.capacity());
    }
}
