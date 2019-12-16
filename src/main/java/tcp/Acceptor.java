package tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * @author Aneureka
 * @createdAt 2019-12-11 11:03
 * @description Accept new socketChannel and offer into the socketChannelQueue
 **/
public class Acceptor implements Runnable {

    private int port;

    private ServerSocketChannel ss;

    private Selector selector;

    private ConcurrentLinkedQueue<SocketChannel> socketChannelQueue;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Acceptor(int port, ConcurrentLinkedQueue<SocketChannel> socketChannelQueue) {
        this.port = port;
        this.socketChannelQueue = socketChannelQueue;
    }

    @Override
    public void run() {
        try {
            selector = Selector.open();
            ss = ServerSocketChannel.open();
            ss.configureBlocking(false);
            ss.bind(new InetSocketAddress(port));
            ss.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            logger.warning(e.getMessage() + " when opening server socket channel");
            e.printStackTrace();
        }
        while (!Thread.interrupted()) {
            try {
                SocketChannel socketChannel = ss.accept();
                if (socketChannel != null) {
                    logger.info("Socket accepted: " + socketChannel);
                    socketChannelQueue.add(socketChannel);
                }
            } catch (IOException e) {
                logger.warning(e.getMessage() + " when accepting socket");
                e.printStackTrace();
            }
        }
    }
}
