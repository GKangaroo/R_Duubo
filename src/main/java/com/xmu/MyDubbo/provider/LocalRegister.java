package com.xmu.MyDubbo.provider;

import java.util.HashMap;
import java.util.Map;


public class LocalRegister {

    private static final Map<String, Class> map = new HashMap<>();

    public static void regist(String interfaceNameAndVersion, Class implClass) {
        map.put(interfaceNameAndVersion, implClass);
    }

    public static Class get(String interfaceNameAndVersion) {
        return map.get(interfaceNameAndVersion);
    }
}
