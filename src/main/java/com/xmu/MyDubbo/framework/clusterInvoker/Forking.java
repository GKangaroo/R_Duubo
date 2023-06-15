package com.xmu.MyDubbo.framework.clusterInvoker;

import com.xmu.MyDubbo.framework.*;
import com.xmu.MyDubbo.framework.loadBalance.LoadBalance;
import com.xmu.MyDubbo.register.Register;
import com.xmu.MyDubbo.util.ReadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Math.max;

/**
 * @Author Frozen_RiceCake
 * @DateTime 2023/6/9 23:45
 */
public class Forking implements ClusterInvoker {
    private static final Logger logger = LoggerFactory.getLogger(Forking.class);
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public Object doInvoker(Class interfaceClass, Invocation invocation, Register register, LoadBalance loadBalance) throws Exception {
        int forks = max(1,
                Integer.parseInt(ReadProperties.readProperties("forking.forks","2")));
        int timeout = max(1000,
                Integer.parseInt(ReadProperties.readProperties("timeout","1000")));
        List<URL> selected = new ArrayList<>();
        List<URL> urlList = register.getURLList(interfaceClass.getSimpleName());
        for(int i=0;i<forks;++i){
            URL url = loadBalance.select(urlList);
            if(!selected.contains(url)){
                selected.add(url);
            }
        }
        logger.debug("urlList:" + urlList);
        logger.debug("selected:" + selected);
        BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<Object>();
        for(URL url:selected){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Protocol protocol = ProtocolFactory.getProtocol();
                        Object result = protocol.send(url, invocation);
                        blockingQueue.offer(result);
                    }
                    catch (Exception ignored){

                    }
                }
            });
        }
        try{
            Object result = blockingQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if(null==result){
                throw new Exception();
            }
            return result;
        }
        catch (Exception e){
            throw new Exception("RPC Invoke("+interfaceClass+":"+invocation+") failed");
        }
    }
}
