# teddy

`teddy` is a tiny but scalable http server implemented with Java NIO, inspired by [netty](https://netty.io/).

### Structure

![image-20191221002609408](https://raw.githubusercontent.com/Aneureka/teddy/master/assets/structure.jpg)

`teddy` is implemented in `Reactor` pattern with Java NIO.

A server instance is equiped with an `Acceptor` and a `Worker` : `Acceptor` accepts upcoming sockets as well as offers them into the `SocketChannelQueue` ; `Worker` binds with a `Selector` and keeps consuming sockets from queue and selecting interested I/O operations.

Each `SocketChannel` is attached with a `ChannelHandlerPipeline` , which handles upstreams from `SocketChannel` and downstreams from application at the top layer. The server is flexible because adding more `ChannelHandler` to the pipeline is allowed, so you can handle both upstream and downstream at a higher layer.



### Progress

🙆‍♀️ TCP layer

🙆‍♀️ HTTP layer: A standard & usable HTTP layer is already implemented, including HTTP message model, decoder, encoder...

👩‍💻 XXX layer: You may add another layer by implementing `ChannelHandler` and add it to `ChannelPipeline`.

