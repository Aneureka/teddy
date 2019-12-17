package core;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 16:41
 * @description
 **/
public class TCPServer {

    private int port;

    private ChannelPipelineFactory pipelineFactory;

    public TCPServer(int port, ChannelPipelineFactory pipelineFactory) {
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
