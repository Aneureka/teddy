package core;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 16:41
 * @description
 **/
public class TcpServer {

    private final static int DEFAULT_PORT = 8080;

    private int port;

    private ChannelPipelineFactory pipelineFactory;

    public TcpServer(ChannelPipelineFactory pipelineFactory) {
        this(DEFAULT_PORT, pipelineFactory);
    }

    public TcpServer(int port, ChannelPipelineFactory pipelineFactory) {
        this.port = port;
        this.pipelineFactory = pipelineFactory;
    }

    public void startServer() {
        ConcurrentLinkedQueue<SocketChannel> channelQueue = new ConcurrentLinkedQueue<>();
        Acceptor acceptor = new Acceptor(port, channelQueue);
        Worker worker = new Worker(channelQueue, pipelineFactory);
        new Thread(acceptor).start();
        new Thread(worker).start();
    }

}
