import core.ChannelBuffer;
import core.ChannelPipeline;
import core.TCPServer;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer {

    public final static int DEFAULT_PORT = 8080;

    private TCPServer tcpServer;

    public HttpServer(int port) {
        this.tcpServer = new TCPServer(port, () -> new ChannelPipeline(new ChannelBuffer(), new HttpProtocol()));
    }

    public HttpServer() {
        this(DEFAULT_PORT);
    }

    public void startServer() {
        tcpServer.startServer();
    }
}
