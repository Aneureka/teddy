import core.ByteToMessageCodec;
import core.ChannelPipeline;
import core.ChannelPipelineFactory;
import core.TcpServer;
import http.HttpMessageCodec;
import http.HttpRequest;

import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:22
 * @description
 **/
public class Main {

    public static void main(String[] args) {
        TcpServer server = new TcpServer((ChannelPipelineFactory) () -> {
            ChannelPipeline pipeline = new ChannelPipeline();
            pipeline.add(new HttpMessageCodec());
            pipeline.add(new HttpServiceHandler());
            return pipeline;
        });
        server.startServer();
    }
}

