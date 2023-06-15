package com.xmu.MyDubbo.framework;

import com.xmu.MyDubbo.framework.clusterInvoker.ClusterInvoker;
import com.xmu.MyDubbo.framework.clusterInvoker.Failover;
import com.xmu.MyDubbo.framework.loadBalance.LoadBalance;
import com.xmu.MyDubbo.register.Register;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;


public class ProxyFactory {
    private static final Logger logger = LoggerFactory.getLogger(ProxyFactory.class);

    public static <T> T getProxy(final Class interfaceClass, String version) {
        Object proxyInstance = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},

                (proxy, method, args) -> {
                    Invocation invocation = new Invocation(interfaceClass.getSimpleName(),
                            method.getName(), version, args, method.getParameterTypes()
                    );
                    // 在注册服务器获取对应服务的URL列表
                    Register register = RegisterFactory.getRegister();
                    LoadBalance loadBalance = LoadBalanceFactory.getLoadBalance();
                    ClusterInvoker clusterInvoker = ClusterInvokerFactory.getClusterInvoker();
                    logger.info(String.valueOf(clusterInvoker.getClass()));
                    return clusterInvoker.doInvoker(interfaceClass, invocation, register, loadBalance);
                });
        return (T) proxyInstance;
    }
}
