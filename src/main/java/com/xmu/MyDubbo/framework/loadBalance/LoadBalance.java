package com.xmu.MyDubbo.framework.loadBalance;

import com.xmu.MyDubbo.framework.URL;

import java.util.List;


public interface LoadBalance {
    public URL select(List<URL> list);
}
