package com.xmu.MyDubbo.framework;

import com.xmu.MyDubbo.framework.clusterInvoker.ClusterInvoker;
import com.xmu.MyDubbo.framework.clusterInvoker.Failfast;
import com.xmu.MyDubbo.util.ReadProperties;

/**
 * @Author Frozen_RiceCake
 * @DateTime 2023/6/10 0:37
 */
public class ClusterInvokerFactory {
    public static ClusterInvoker getClusterInvoker() {
        String name = ReadProperties.readProperties("clusterInvoker","Failfast");
        StringBuilder fullName = new StringBuilder()
                .append(ClusterInvoker.class.getPackageName()).append('.').append(name);
        try {
            return (ClusterInvoker)Class.forName(fullName.toString()).getConstructor().newInstance();
        }
        catch (Exception ignored){

        }
        return new Failfast();
    }
}
