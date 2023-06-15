package com.xmu.MyDubbo.provider.service;

import com.xmu.MyDubbo.entity.User;


public interface HelloServiceInterface {

    String sayHello(String name);

    String sayHello(User user);
}
