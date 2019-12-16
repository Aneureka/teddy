package tcp;

import java.nio.channels.SelectionKey;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 17:15
 * @description
 **/
public interface ChannelHandler {

    void handleRead(SelectionKey key);

    void handleWrite(SelectionKey key);

}
