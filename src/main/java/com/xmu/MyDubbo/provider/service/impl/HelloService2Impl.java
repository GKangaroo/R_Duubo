package com.xmu.MyDubbo.provider.service.impl;

import com.xmu.MyDubbo.entity.User;
import com.xmu.MyDubbo.provider.service.HelloServiceInterface;


public class HelloService2Impl implements HelloServiceInterface {

    public static final String VERSION = "2";

    @Override
    public String sayHello(String name) {
        return "Hello " + name + "! version: " + VERSION;
    }

    @Override
    public String sayHello(User user) {
        return "Hello " + user + "! version: " + VERSION;
    }
}
