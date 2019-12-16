package tcp;

import java.nio.channels.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 16:41
 * @description
 **/
public class TCPServer {

    private int port;

    private ChannelHandler handler;

    public TCPServer(int port, ChannelHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void startServer() {
        ConcurrentLinkedQueue<SocketChannel> channelQueue = new ConcurrentLinkedQueue<>();
        Acceptor acceptor = new Acceptor(port, channelQueue);
        Worker worker = new Worker(channelQueue, handler);
        new Thread(acceptor).start();
        new Thread(worker).start();
    }

}
