import core.ChannelHandler;
import core.ChannelHandlerContext;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-19 11:11
 * @description
 **/
public abstract class ByteToMessageCodec<T> implements ChannelHandler {

    private List<Byte> accumulation;

    public ByteToMessageCodec() {
        this.accumulation = new ArrayList<>();
    }

    /**
     * Consume the bytes until being successful to construct a target or failed.
     * @param in
     * @param out
     * @return
     */
    protected abstract boolean decode(List<Byte> in, List<T> out);

    /**
     * Accept a object and convert it to list of byte.
     * @param in
     * @param out
     * @return
     */
    protected abstract boolean encode(Object in, List<Byte> out);

    @Override
    public void handleUpStream(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuffer) {
            // Read content of buffer into accumulation.
            ByteBuffer buffer = (ByteBuffer) msg;
            buffer.flip();
            while (true) {
                try {
                    accumulation.add(buffer.get());
                } catch (BufferUnderflowException e) {
                    break;
                }
            }
            // Decode accumulated bytes to target object.
            List<T> out = new ArrayList<>();
            boolean successful = decode(accumulation, out);
            if (successful) {
                for (T t : out) {
                    ctx.sendUpstream(t);
                }
            }
        } else {
            ctx.sendUpstream(msg);
        }
    }

    @Override
    public void handleDownStream(ChannelHandlerContext ctx, Object msg) {
        List<Byte> out = new ArrayList<>();
        boolean successful = encode(msg, out);
        if (successful) {
            ByteBuffer buffer = convertToByteBuffer(out);
            ctx.sendDownStream(buffer);
        } else {
            ctx.sendDownStream(msg);
        }
    }

    private ByteBuffer convertToByteBuffer(List<Byte> byteList) {
        int n = byteList.size();
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++) {
            bytes[i] = byteList.get(i);
        }
        return ByteBuffer.wrap(bytes);
    }
}
