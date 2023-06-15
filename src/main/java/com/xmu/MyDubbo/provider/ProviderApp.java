package com.xmu.MyDubbo.provider;

import com.xmu.MyDubbo.framework.Protocol;
import com.xmu.MyDubbo.framework.ProtocolFactory;
import com.xmu.MyDubbo.framework.RegisterFactory;
import com.xmu.MyDubbo.framework.URL;
import com.xmu.MyDubbo.provider.service.HelloServiceInterface;
import com.xmu.MyDubbo.provider.service.ShopServiceInterface;
import com.xmu.MyDubbo.provider.service.impl.HelloService1Impl;
import com.xmu.MyDubbo.provider.service.impl.HelloService2Impl;
import com.xmu.MyDubbo.provider.service.impl.ShopService1Impl;
import com.xmu.MyDubbo.register.Register;
import com.xmu.MyDubbo.util.ReadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProviderApp {
    private static final Logger logger = LoggerFactory.getLogger(ProviderApp.class);
    public static void main(String[] args) {

        // 注册服务（接口名 + 版本号，实现多版本）
        // HelloService1: HelloService1Impl.class
        // HelloService2: HelloService2Impl.class

        // 本地注册
        LocalRegister.regist(HelloServiceInterface.class.getSimpleName() + HelloService1Impl.VERSION,
                HelloService1Impl.class);
        LocalRegister.regist(HelloServiceInterface.class.getSimpleName() + HelloService2Impl.VERSION,
                HelloService2Impl.class);
        LocalRegister.regist(ShopServiceInterface.class.getSimpleName() + ShopService1Impl.VERSION,
                ShopService1Impl.class);

        String serviceIP = ReadProperties.readProperties("serviceIP");
        Integer servicePort = Integer.valueOf(ReadProperties.readProperties("servicePort"));
        // 注册中心注册 Zookeeper
        URL url = new URL(serviceIP, servicePort,60);
        Register register = RegisterFactory.getRegister();
        register.register(HelloServiceInterface.class.getSimpleName(), url);
        register.register(ShopServiceInterface.class.getSimpleName(), url);

        // 接收请求 netty http ...
        Protocol protocol = ProtocolFactory.getProtocol();
        logger.info("启动对 " + url + " 的监听。");
        protocol.start(url);

        // 添加关闭的Hook，使provider在退出时自动注销服务
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            register.unregister(HelloServiceInterface.class.getSimpleName(), url);
            register.unregister(ShopServiceInterface.class.getSimpleName(), url);
        }));
    }
}
