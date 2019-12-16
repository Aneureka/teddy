import tcp.ChannelHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 19:31
 * @description
 **/
public class HttpProtocol implements ChannelHandler {

    public final static int DEFAULT_BUFFER_SIZE = 32;

    private int bufferSize;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public HttpProtocol(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public HttpProtocol() {
        this(DEFAULT_BUFFER_SIZE);
    }

    @Override
    public void handleRead(SelectionKey key) {
        // todo
        System.out.println("reading...");
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        System.out.println("reading here");
        try {
            long bytesRead = 0;
            do {
                bytesRead = socketChannel.read(buffer);
                System.out.println(bytesRead);
                StringBuilder messageBuilder = new StringBuilder();
                if (bytesRead == bufferSize) {
                    messageBuilder.append(new String(buffer.array()));
                    buffer.clear();
                } else if (bytesRead >= 0) {
                    key.attach(messageBuilder.toString());
                    System.out.println("message " + messageBuilder.toString());
                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (bytesRead == -1) {
                    socketChannel.close();
                }
            } while (bytesRead > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleWrite(SelectionKey key) {
        // todo
        System.out.println("writing...");
        String msg = (String) key.attachment();
        byte[] byteMsg = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        buffer.flip();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            int i = 0;
            while (i < byteMsg.length) {
                buffer.put(byteMsg, i, i + Math.min(bufferSize, byteMsg.length - i));
                socketChannel.write(buffer);
                i += Math.min(bufferSize, msg.length() - i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!buffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
        }
        buffer.compact();
    }

}
