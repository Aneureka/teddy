import core.ChannelPipeline;
import core.TcpServer;
import example.HttpServiceHandler;
import http.HttpMessageCodec;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:22
 * @description
 **/
public class Main {

    public static void main(String[] args) {
        TcpServer server = new TcpServer(() -> {
            ChannelPipeline pipeline = new ChannelPipeline();
            pipeline.add(new HttpMessageCodec());
            pipeline.add(new HttpServiceHandler());
            return pipeline;
        });
        server.startServer();
    }
}

