package tcp;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * @author Aneureka
 * @createdAt 2019-12-11 11:37
 * @description Take channels from socketChannelQueue and process them
 **/
public class Worker implements Runnable {

    private SelectionKey key;

    private ChannelHandler handler;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Worker(SelectionKey key, ChannelHandler handler) {
        this.key = key;
        this.handler = handler;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            if (key.isReadable()) {
                handler.handleRead(key);
            }
            if (key.isWritable()) {
                handler.handleWrite(key);
            }
        }
    }

}
