import tcp.TCPServer;

import java.io.IOException;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer {

    public final static int DEFAULT_PORT = 8080;

    private TCPServer tcpServer;

    public HttpServer(int port) {
        this.tcpServer = new TCPServer(port, new HttpProtocol());
    }

    public HttpServer() {
        this(DEFAULT_PORT);
    }

    public void startServer() throws IOException {
        tcpServer.startServer();
    }
}
