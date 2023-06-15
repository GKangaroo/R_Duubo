package com.xmu.MyDubbo.framework.protocol.dubbo;

import com.xmu.MyDubbo.framework.Invocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private Invocation invocation;
    private Object result;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        result = msg;
        notify();
    }

    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(this.invocation);
        wait();
        return result;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }
}
