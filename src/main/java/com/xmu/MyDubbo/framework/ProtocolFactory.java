package com.xmu.MyDubbo.framework;

import com.xmu.MyDubbo.framework.protocol.dubbo.DubboProtocol;
import com.xmu.MyDubbo.framework.protocol.http.HttpProtocol;
import com.xmu.MyDubbo.util.ReadProperties;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Frozen_RiceCake
 * @DateTime 2023/5/17 16:47
 */
public class ProtocolFactory {

    public static Protocol getProtocol() {
        // 工厂模式
        String name = ReadProperties.readProperties("protocolName");
        if (null == name || "".equals(name)) {
            name = "dubbo";
        }
        switch (name) {
            case "http":
                return new HttpProtocol();
            case "dubbo":
                return new DubboProtocol();
            default:
                break;
        }
        return new DubboProtocol();
    }
}
