package tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 16:41
 * @description
 **/

public class TCPServer implements Runnable {

    public final static int DEFAULT_TIMEOUT = 5000;

    private Protocol protocol;

    private int port;

    private int timeout;

    public TCPServer(Protocol protocol, int port, int timeout) {
        this.protocol = protocol;
        this.port = port;
        this.timeout = timeout;
    }

    public TCPServer(Protocol protocol, int port) {
        this(protocol, port, DEFAULT_TIMEOUT);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void startServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel listeningChannel = ServerSocketChannel.open();
        listeningChannel.configureBlocking(false);
        listeningChannel.socket().bind(new InetSocketAddress(port));
        listeningChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(timeout) == 0) {
                continue;
            }
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    protocol.handleAccept(key);
                }
                if (key.isReadable()) {
                    protocol.handleRead(key);
                }
                if (key.isValid() && key.isWritable()) {
                    protocol.handleWrite(key);
                }
                keyIterator.remove();
            }
        }
    }

    @Override
    public void run() {
        try {
            this.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
