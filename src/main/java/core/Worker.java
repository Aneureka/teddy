package core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Aneureka
 * @createdAt 2019-12-15 21:24
 * @description
 **/
public class Worker implements Runnable {

    private ConcurrentLinkedQueue<SocketChannel> socketChannelQueue;

    private volatile Selector selector;

    private ChannelPipelineFactory pipelineFactory;

    public Worker(ConcurrentLinkedQueue<SocketChannel> socketChannelQueue, ChannelPipelineFactory pipelineFactory) {
        this.socketChannelQueue = socketChannelQueue;
        this.pipelineFactory = pipelineFactory;
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Override
    public void run() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // This thread is used for polling new accepted channels from queue.
        new Thread(() -> {
            while (!Thread.interrupted()) {
                SocketChannel channel = socketChannelQueue.poll();
                if (channel == null) {
                    continue;
                }
                try {
                    channel.configureBlocking(false);
                    ChannelPipeline pipeline = pipelineFactory.getPipeline();
                    pipeline.bind(channel);
                    channel.register(selector, SelectionKey.OP_READ, pipeline);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        // This thread is used for selecting.
        new Thread(() -> {
            try {
                while (!Thread.interrupted()) {
                    if (selector.selectNow() == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        SocketChannel channel = (SocketChannel) key.channel();
                        ChannelPipeline channelPipeline = (ChannelPipeline) key.attachment();
                        if (!channelPipeline.bound()) {
                            channelPipeline.bind(channel);
                        }
                        if (key.isReadable()) {
                            channelPipeline.sendUpstream();
                            if (key.isValid()) {
                                key.interestOps(SelectionKey.OP_WRITE);
                            }
                        }
                        if (key.isValid() && key.isWritable()) {
                            channelPipeline.sendDownStream();
                            key.interestOps(SelectionKey.OP_READ);
                        }
                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
