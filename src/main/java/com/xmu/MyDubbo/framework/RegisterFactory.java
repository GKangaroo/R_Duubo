package com.xmu.MyDubbo.framework;

import com.xmu.MyDubbo.register.Register;
import com.xmu.MyDubbo.register.LocalMapRegister;
import com.xmu.MyDubbo.register.ZookeeperRegister;
import com.xmu.MyDubbo.util.ReadProperties;


public class RegisterFactory {
    public static Register getRegister() {
        String name = ReadProperties.readProperties("registerName");
        if (null == name || "".equals(name)) {
            name = "local";
        }
        switch (name) {
            case "local":
                return new LocalMapRegister();
            case "zookeeper":
                return new ZookeeperRegister();
            default:
                break;
        }
        return new LocalMapRegister();
    }
}
