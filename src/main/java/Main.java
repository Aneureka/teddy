import core.ByteToMessageCodec;
import core.ChannelPipeline;
import core.TcpServer;
import http.HttpRequest;

import java.util.List;

/**
 * @author Aneureka
 * @createdAt 2019-12-09 20:22
 * @description
 **/
public class Main {

    public static void main(String[] args) {
        TcpServer server = new TcpServer(() -> new ChannelPipeline(new ByteToMessageCodec<HttpRequest>() {
            @Override
            protected boolean decode(List<Byte> in, List<HttpRequest> out) {
                in.clear();
                return false;
            }
            @Override
            protected boolean encode(Object in, List<Byte> out) {
                byte[] bytesToWrite = ("HTTP/1.1 200 OK\r\n" +
                        "Content-Length: 38\r\n" +
                        "Content-Type: text/html\r\n" +
                        "\r\n" +
                        "<html><body>Hello World!</body></html><br/>").getBytes();
                for (byte b : bytesToWrite) {
                    out.add(b);
                }
                return true;
            }
        }));
        server.startServer();
    }
}

