package com.xmu.MyDubbo.framework.clusterInvoker;

import com.xmu.MyDubbo.framework.Invocation;
import com.xmu.MyDubbo.framework.loadBalance.LoadBalance;
import com.xmu.MyDubbo.register.Register;

import java.util.List;
/**
 * @Author Frozen_RiceCake
 * @DateTime 2023/6/9 16:50
 */
public interface ClusterInvoker {
    public Object doInvoker(Class interfaceClass, Invocation invocation, Register register, LoadBalance loadBalance) throws Exception;
}
