package com.xmu.MyDubbo.util;

import com.xmu.MyDubbo.framework.ProtocolFactory;

import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
    public static String readProperties(String name){
        return readProperties(name, null);
    }
    public static String readProperties(String name, String defaultValue){
        Properties p = new Properties();
        InputStream in = ProtocolFactory.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            p.load(in);
            return p.getProperty(name, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
