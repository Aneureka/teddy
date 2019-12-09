package tcp;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 19:27
 * @description
 **/
public interface Protocol {

    void handleAccept(SelectionKey key) throws IOException;

    void handleRead(SelectionKey key) throws IOException;

    void handleWrite(SelectionKey key) throws IOException;

}
