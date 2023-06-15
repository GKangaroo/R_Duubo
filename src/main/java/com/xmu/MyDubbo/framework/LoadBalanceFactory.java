package com.xmu.MyDubbo.framework;

import com.xmu.MyDubbo.framework.loadBalance.LoadBalance;

import com.xmu.MyDubbo.framework.loadBalance.RandomSelect;
import com.xmu.MyDubbo.util.ReadProperties;

/**
 * @Author Frozen_RiceCake
 * @DateTime 2023/6/9 10:38
 */
public class LoadBalanceFactory {
    public static LoadBalance getLoadBalance() {
        String name = ReadProperties.readProperties("loadBalance","RandomSelect");
        StringBuilder fullName = new StringBuilder()
                .append(LoadBalance.class.getPackageName()).append('.').append(name);
        try {
            return (LoadBalance)Class.forName(fullName.toString()).getConstructor().newInstance();
        }
        catch (Exception ignored){

        }
        return new RandomSelect();
    }
}
