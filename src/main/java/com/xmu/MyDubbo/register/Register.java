package com.xmu.MyDubbo.register;

import com.xmu.MyDubbo.framework.URL;

import java.util.List;

public interface Register {

    void register(String interfaceName, URL url);
    void unregister(String interfaceName, URL url);

    List<URL> getURLList(String interfaceName);
}
