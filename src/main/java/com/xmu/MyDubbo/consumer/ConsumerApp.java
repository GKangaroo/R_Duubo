package com.xmu.MyDubbo.consumer;

import com.xmu.MyDubbo.framework.ProxyFactory;
import com.xmu.MyDubbo.provider.service.ShopServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConsumerApp {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerApp.class);
    public static void main(String[] args) {

//        HelloService helloService = ProxyFactory
//                .getProxy(HelloService.class, "3");
//        User user = new User("kaola", 6);



        logger.debug("发起RPC调用。");
//        for(int i=0;i<=10;++i){
//            String result = shopService.getShop();
//            logger.info("result:" + result);
//        }
        ShopServiceInterface shopServiceInterface = ProxyFactory
                .getProxy(ShopServiceInterface.class, "1");
        String result = shopServiceInterface.getShop("1");
        logger.info("result:" + result);
        logger.debug("RPC调用完毕。");

    }
}
