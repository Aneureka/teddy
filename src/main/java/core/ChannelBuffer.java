package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 17:37
 * @description
 **/
public class ChannelBuffer {

    public final static int BUFFER_SIZE = 1024 * 8;

    private List<Byte> readBuffer;

    private List<Byte> writeBuffer;

    public ChannelBuffer() {
        this.readBuffer = new ArrayList<>();
        this.writeBuffer = new ArrayList<>();
    }

    private static byte[] array(List<Byte> buffer) {
        byte[] res = new byte[buffer.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = buffer.get(i);
        }
        return res;
    }

    public void addToReadBuffer(ByteBuffer buffer, long length) {
        add(this.readBuffer, buffer, length);
    }

    public void addToWriteBuffer(ByteBuffer buffer, long length) {
        if (buffer == null) return;
        add(this.writeBuffer, buffer, length);
    }

    public int pullFromReadBuffer(ByteBuffer buffer) {
        return pull(this.readBuffer, buffer);
    }

    public int pullFromWriteBuffer(ByteBuffer buffer) {
        return pull(this.writeBuffer, buffer);
    }

    private static void add(List<Byte> bufferToAppend, ByteBuffer buffer, long length) {
        for (int i = 0; i < length; i++) {
            bufferToAppend.add(buffer.get(i));
        }
    }

    private static int pull(List<Byte> bufferToPull, ByteBuffer buffer) {
        int nBytesToPull = Math.min(bufferToPull.size(), buffer.capacity());
        buffer.clear();
        for (int i = 0; i < nBytesToPull; i++) {
            buffer.put(bufferToPull.get(0));
            bufferToPull.remove(0);
        }
        return nBytesToPull;
    }

    @Override
    public String toString() {
        return String.format("=== [ReadBuffer] ===\n%s\n=== [WriteBuffer] ===\n%s", new String(array(readBuffer)), new String(array(writeBuffer)));
    }

    /**
     * Read bytes from socket channel.
     * @param socketChannel
     * @return status code: 0 or -1
     */
    public void readFromChannel(SocketChannel socketChannel) throws ClosedChannelException {
        System.out.println("Reading...");
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        long bytesRead = 0;
        try {
            bytesRead = socketChannel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.addToReadBuffer(buffer, bytesRead);
        if (bytesRead == -1) {
            throw new ClosedChannelException();
        }
    }

    public void writeToChannel(SocketChannel socketChannel, Protocol protocol) {
        System.out.println("Writing...");
        protocol.process(this);
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            while (this.pullFromWriteBuffer(buffer) != 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
