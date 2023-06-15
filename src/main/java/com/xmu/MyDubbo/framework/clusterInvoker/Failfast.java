package com.xmu.MyDubbo.framework.clusterInvoker;

import com.xmu.MyDubbo.framework.*;
import com.xmu.MyDubbo.framework.loadBalance.LoadBalance;
import com.xmu.MyDubbo.register.Register;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * @Author Frozen_RiceCake
 * @DateTime 2023/6/9 17:02
 */
public class Failfast implements ClusterInvoker {
    private static final Logger logger = LoggerFactory.getLogger(Failfast.class);
    @Override
    public Object doInvoker(Class interfaceClass, Invocation invocation, Register register, LoadBalance loadBalance) throws Exception {
        try{
            List<URL> urlList = register.getURLList(interfaceClass.getSimpleName());
            URL url = loadBalance.select(urlList);
            logger.debug("URL:" + url);
            Protocol protocol = ProtocolFactory.getProtocol();
            Object result = protocol.send(url, invocation);
            return result;
        }
        catch (Exception e) {
            throw new Exception("RPC Invoke(" + interfaceClass + ":" + invocation + ") failed");
        }
    }
}
