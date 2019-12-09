import tcp.Protocol;
import tcp.TCPServer;

import java.io.IOException;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer implements Runnable {

    public final static int DEFAULT_PORT = 8080;

    private TCPServer tcpServer;

    public HttpServer(int port) {
        this.tcpServer = new TCPServer(new HttpProtocol(), port);
    }

    public HttpServer() {
        this(DEFAULT_PORT);
    }

    @Override
    public void run() {
        tcpServer.run();
    }
}
