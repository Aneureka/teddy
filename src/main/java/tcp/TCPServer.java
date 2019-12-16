package tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 16:41
 * @description
 **/
public class TCPServer {

    public final static int DEFAULT_TIMEOUT = 5000;

    private int port;

    private ChannelHandler handler;

    public TCPServer(int port, ChannelHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void startServer() throws IOException {
        ConcurrentLinkedQueue<SocketChannel> channelQueue = new ConcurrentLinkedQueue<>();
        Acceptor acceptor = new Acceptor(port, channelQueue);
        WorkerPool workers = new WorkerPool(channelQueue, handler);
        new Thread(acceptor).start();
        new Thread(workers).start();
    }

}
