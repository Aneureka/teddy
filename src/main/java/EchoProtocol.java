import tcp.Protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 19:45
 * @description
 **/
public class EchoProtocol implements Protocol {

    private int bufferSize;

    public EchoProtocol(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        long bytesRead = socketChannel.read(buffer);
        if (bytesRead > 0) {
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } else if (bytesRead == -1) {
            socketChannel.close();
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
        }
        buffer.compact();
    }
}
