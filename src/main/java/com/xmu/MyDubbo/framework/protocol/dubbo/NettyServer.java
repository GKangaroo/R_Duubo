package com.xmu.MyDubbo.framework.protocol.dubbo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;



public class NettyServer {

    public void start(String hostName, int port) {
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workerGroup  = new NioEventLoopGroup();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("logger",new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers
                                    .weakCachingConcurrentResolver(this.getClass()
                                            .getClassLoader())));
                            pipeline.addLast("encoder", new ObjectEncoder());
                            pipeline.addLast("handler", new NettyServerHandler());
                        }
                    });
            serverBootstrap.bind(hostName, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
