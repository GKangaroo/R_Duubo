package com.xmu.MyDubbo.framework.protocol.dubbo;

import com.xmu.MyDubbo.framework.Invocation;
import com.xmu.MyDubbo.framework.Protocol;
import com.xmu.MyDubbo.framework.URL;


public class DubboProtocol implements Protocol {

    @Override
    public void start(URL url) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(url.getHostname(), url.getPort());
    }

    @Override
    public Object send(URL url, Invocation invocation) {
        NettyClient nettyClient = new NettyClient();
        return nettyClient.send(url.getHostname(), url.getPort(), invocation);
    }
}
