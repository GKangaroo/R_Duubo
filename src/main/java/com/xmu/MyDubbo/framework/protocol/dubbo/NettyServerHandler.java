package com.xmu.MyDubbo.framework.protocol.dubbo;

import com.xmu.MyDubbo.framework.FilterFactory;
import com.xmu.MyDubbo.framework.Invocation;
import com.xmu.MyDubbo.framework.filter.Filter;
import com.xmu.MyDubbo.provider.LocalRegister;
import com.xmu.MyDubbo.provider.ProviderApp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.Method;


public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    private static final Filter filter = FilterFactory.getFilter();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(filter.isAllowed()) {
            // 调用对象
            Invocation invocation = (Invocation) msg;
            logger.info("invocation: " + invocation);
            // 获取实现类
            Class implClass = LocalRegister.get(invocation.getInterfaceName() + invocation.getVersion());
            // 由方法名参数类型列表确定调用的方法
            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamType());
            // 通过反射机制执行方法
            Object result = method.invoke(implClass.getConstructor().newInstance(), invocation.getParams());
            logger.info("result: " + result);
            // 写回
            ctx.writeAndFlush(result);
        }else{
            // 服务降级放在这里
            ctx.writeAndFlush("provider端到达流量上限");
        }
    }
}
