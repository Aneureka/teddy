package http;

import core.ChannelPipeline;
import core.TcpServer;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:17
 * @description
 **/
public class HttpServer {

    private TcpServer tcpServer;

    public HttpServer() {
        this.tcpServer = new TcpServer(() -> new ChannelPipeline(new HttpMessageCodec()));
    }

    public HttpServer(int port) {
        this.tcpServer = new TcpServer(port, () -> new ChannelPipeline(new HttpMessageCodec()));
    }

    public void startServer() {
        tcpServer.startServer();
    }
}
