package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ChannelBuffer implements ChannelHandler {

    public final static int BUFFER_SIZE = 1024 * 8;

    private List<Byte> readBuffer;

    private List<Byte> writeBuffer;

    public ChannelBuffer() {
        this.readBuffer = new ArrayList<>();
        this.writeBuffer = new ArrayList<>();
    }

    /**
     * Append content of buffer that will be read then.
     *
     * @param buffer
     * @param length
     */
    public void addToRead(ByteBuffer buffer, int length) {
        add(this.readBuffer, buffer, length);
    }

    /**
     * Append content of buffer that will be written then.
     *
     * @param buffer
     * @param length
     */
    public void addToWrite(ByteBuffer buffer, int length) {
        add(this.writeBuffer, buffer, length);
    }

    private static void add(List<Byte> bufferToAdd, ByteBuffer buffer, int length) {
        if (buffer == null) return;
        for (int i = 0; i < length; i++) {
            bufferToAdd.add(buffer.get(i));
        }
    }

    /**
     * Insert content of buffer in the front of read buffer.
     *
     * @param buffer
     * @param length
     */
    public void addFirstToRead(ByteBuffer buffer, int length) {
        addFirst(readBuffer, buffer, length);
    }

    /**
     * Insert content of buffer in the front of write buffer.
     *
     * @param buffer
     * @param length
     */
    public void addFirstToWrite(ByteBuffer buffer, int length) {
        addFirst(writeBuffer, buffer, length);
    }

    private static void addFirst(List<Byte> bufferToAdd, ByteBuffer buffer, int length) {
        if (buffer == null) return;
        for (int i = 0; i < length; i++) {
            bufferToAdd.add(0, buffer.get(i));
        }
    }

    /**
     * Poll all the content of read buffer.
     *
     * @return
     */
    public ByteBuffer pollAllRead() {
        return pollAll(this.readBuffer);
    }

    /**
     * Poll all the content of write buffer.
     *
     * @return
     */
    public ByteBuffer pollAllWrite() {
        return pollAll(this.writeBuffer);
    }

    private static ByteBuffer pollAll(List<Byte> bufferToPoll) {
        ByteBuffer buffer = ByteBuffer.allocate(length(bufferToPoll));
        poll(bufferToPoll, buffer);
        return buffer;
    }

    /**
     * poll content from read buffer to be processed by concrete protocol.
     *
     * @param buffer
     * @return
     */
    public int pollRead(ByteBuffer buffer) {
        return poll(this.readBuffer, buffer);
    }

    /**
     * poll content from write buffer that will be written to socket channel.
     *
     * @param buffer
     * @return
     */
    public int pollWrite(ByteBuffer buffer) {
        return poll(this.writeBuffer, buffer);
    }

    private static int poll(List<Byte> bufferToPoll, ByteBuffer buffer) {
        int nBytesToPoll = Math.min(bufferToPoll.size(), buffer.capacity());
        buffer.clear();
        for (int i = 0; i < nBytesToPoll; i++) {
            buffer.put(bufferToPoll.get(0));
            bufferToPoll.remove(0);
        }
        return nBytesToPoll;
    }

    /**
     * Inquire the length of read buffer.
     *
     * @return
     */
    public int lengthToRead() {
        return length(this.readBuffer);
    }

    /**
     * Inquire the length of write buffer.
     *
     * @return
     */
    public int lengthToWrite() {
        return length(this.writeBuffer);
    }

    private static int length(List<Byte> buffer) {
        return buffer.size();
    }

    private static byte[] array(List<Byte> buffer) {
        byte[] res = new byte[buffer.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = buffer.get(i);
        }
        return res;
    }

    /**
     * Read from socket channel with a fixed-length buffer.
     *
     * @param socketChannel
     * @throws ClosedChannelException
     */
    private void readFromChannel(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int bytesRead = 0;
        try {
            bytesRead = socketChannel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.addToRead(buffer, bytesRead);
        if (bytesRead == -1) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Write <b>all</b> the content of write buffer to socket channel.
     *
     * @param socketChannel
     */
    private void writeToChannel(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            while (this.pollWrite(buffer) != 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("=== [ReadBuffer] ===\n%s\n=== [WriteBuffer] ===\n%s", new String(array(readBuffer)), new String(array(writeBuffer)));
    }

    @Override
    public void read(Object object) {
        if (object instanceof SocketChannel) {
            SocketChannel channel = (SocketChannel) object;
            this.readFromChannel(channel);
        }
    }

    @Override
    public void write(Object object) {
        if (object instanceof SocketChannel) {
            SocketChannel channel = (SocketChannel) object;
            this.writeToChannel(channel);
        }
    }
}
