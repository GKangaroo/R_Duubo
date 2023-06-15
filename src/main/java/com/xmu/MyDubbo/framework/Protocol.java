package com.xmu.MyDubbo.framework;

public interface Protocol {

    void start(URL url);

    Object send(URL url, Invocation invocation);
}
