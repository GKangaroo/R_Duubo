package com.xmu.MyDubbo.framework.clusterInvoker;

import com.xmu.MyDubbo.framework.*;
import com.xmu.MyDubbo.framework.loadBalance.LoadBalance;
import com.xmu.MyDubbo.register.Register;
import com.xmu.MyDubbo.util.ReadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.lang.Math.max;
/**
 * @Author Frozen_RiceCake
 * @DateTime 2023/6/9 17:24
 */
// 调用n次
public class Failover implements ClusterInvoker {
    private static final Logger logger = LoggerFactory.getLogger(Failover.class);
    @Override
    public Object doInvoker(Class interfaceClass, Invocation invocation, Register register, LoadBalance loadBalance) throws Exception {
        int retries = max(0,
                Integer.parseInt(ReadProperties.readProperties("failover.retries","2")));
        for(int i=0;i<=retries;++i){
            try{
                List<URL> urlList = register.getURLList(interfaceClass.getSimpleName());
                logger.debug("urlList:" + urlList);
                URL url = loadBalance.select(urlList);
                logger.debug("URL:" + url);
                Protocol protocol = ProtocolFactory.getProtocol();
                Object result = protocol.send(url, invocation);
                return result;
            }
            catch (Exception ignored){

            }
        }
        throw new Exception("RPC Invoke("+interfaceClass+":"+invocation+") failed");
    }
}
