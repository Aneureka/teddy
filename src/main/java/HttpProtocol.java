import tcp.Protocol;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 19:31
 * @description
 **/
public class HttpProtocol implements Protocol {

    public final static int DEFAULT_BUFFER_SIZE = 1024;

    private int bufferSize;

    public HttpProtocol(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public HttpProtocol() {
        this(DEFAULT_BUFFER_SIZE);
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        // todo
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        // todo
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        // todo
    }
}
