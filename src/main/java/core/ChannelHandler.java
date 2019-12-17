package core;

/**
 * @author Aneureka
 * @createdAt 2019-12-17 16:22
 * @description
 **/
public interface ChannelHandler {
    void read(Object object);
    void write(Object object);
}
