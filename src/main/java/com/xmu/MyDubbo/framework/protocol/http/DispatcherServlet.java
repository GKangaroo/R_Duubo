package com.xmu.MyDubbo.framework.protocol.http;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // 处理请求的逻辑
        new HttpServerHandler().handler(req, resp);
    }
}
