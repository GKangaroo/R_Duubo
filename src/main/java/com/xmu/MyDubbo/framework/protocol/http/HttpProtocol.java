package com.xmu.MyDubbo.framework.protocol.http;

import com.xmu.MyDubbo.framework.Invocation;
import com.xmu.MyDubbo.framework.Protocol;
import com.xmu.MyDubbo.framework.URL;


public class HttpProtocol implements Protocol {

    @Override
    public void start(URL url) {
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }

    @Override
    public Object send(URL url, Invocation invocation) {
        HttpClient httpClient = new HttpClient();
        return httpClient.send(url.getHostname(), url.getPort(), invocation);
    }
}
