package com.xmu.MyDubbo.framework.protocol.dubbo;

import com.xmu.MyDubbo.framework.Invocation;
import com.xmu.MyDubbo.util.ReadProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;


public class NettyClient {

    private NettyClientHandler client = null;
    private ChannelFuture future = null;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    public void start(String hostName, Integer port) {
        int timeout = max(1000,
                Integer.parseInt(ReadProperties.readProperties("timeout","1000")));
        client = new NettyClientHandler();

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                // 最大延迟1s
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers
                                .weakCachingConcurrentResolver(this.getClass()
                                        .getClassLoader())));
                        pipeline.addLast("encoder", new ObjectEncoder());
                        pipeline.addLast("handler", client);
                    }
                });

        try {
            future = bootstrap.connect(hostName, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Object send(String hostName, Integer port, Invocation invocation) {

        if (client == null) {
            start(hostName, port);
        }

        client.setInvocation(invocation);
        Object ret = null;
        try {
            ret = executorService.submit(client).get();
            future.channel().close().sync();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return ret;
    }

}
