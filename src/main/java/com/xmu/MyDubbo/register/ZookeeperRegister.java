package com.xmu.MyDubbo.register;

import com.alibaba.fastjson.JSONObject;
import com.xmu.MyDubbo.framework.URL;
import com.xmu.MyDubbo.util.ReadProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ZookeeperRegister implements Register {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegister.class);
    static CuratorFramework client;

    static {
        String zookeeperIP = ReadProperties.readProperties("zookeeperIP");
        String zookeeperPort = ReadProperties.readProperties("zookeeperPort", "2181");
        StringBuilder url = new StringBuilder().append(zookeeperIP).append(':').append(zookeeperPort);
        client = CuratorFrameworkFactory
                .newClient(url.toString(), new RetryNTimes(3, 1000));
        client.start();
    }
    //调用是从zookeeper中实时获取url，不会用上此Map
//    private static final Map<String, List<URL>> REGISTER = new HashMap<>();
/*
regist函数的作用是：
创建一个Zookeeper客户端实例，连接到指定的Zookeeper服务器。
以临时节点的方式，在/dubbo/service/接口名/URL路径下写入服务提供者的URL地址，其中URL是一个JSON对象，包含了服务提供者的主机名，端口号，协议，参数等信息。
打印出创建的节点的路径，并输出“Registered through ZookeeperRegister.”的日志信息。
如果发生异常，打印出异常堆栈信息。
 */
    @Override
    public void register(String interfaceName, URL url) {
        try {
            String result = client
                    .create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(String.format("/dubbo/service/%s/%s", interfaceName, JSONObject.toJSONString(url)), null);
            logger.info(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Registered through ZookeeperRegister.");
    }

    // register采用的注册模式是CreateMode.EPHEMERAL，创建的是临时节点
    // 虽然对话session结束后自动删除临时节点，但这会有一定延迟，因此还是手动注销
    @Override
    public void unregister(String interfaceName, URL url) {
        try {
            client
                    .delete()
                    .forPath(String.format("/dubbo/service/%s/%s", interfaceName, JSONObject.toJSONString(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Unregistered through ZookeeperRegister.");
    }

    @Override
    public List<URL> getURLList(String interfaceName) {

        List<URL> urlList = new ArrayList<>();

        try {
            List<String> result = client
                    .getChildren()
                    .forPath(String.format("/dubbo/service/%s", interfaceName));

            for (String urlstr : result) {
                urlList.add(JSONObject.parseObject(urlstr, URL.class));
            }

//            REGISTER.put(interfaceName, urlList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Get the URL from ZookeeperRegister.");
        return urlList;
    }
}