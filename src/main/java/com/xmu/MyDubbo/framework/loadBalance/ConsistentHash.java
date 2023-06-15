package com.xmu.MyDubbo.framework.loadBalance;

import com.xmu.MyDubbo.framework.URL;

import java.security.MessageDigest;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author：guiqingxin
 * @date：2023/6/1 9:59
 */
public class ConsistentHash implements LoadBalance{

    @Override
    public URL select(List<URL> list) {
        //通过selector选择list中的一个url
        //首先创建一个selector对象，传入虚拟节点数
        selector sel = new selector(100);
        //然后遍历list，将每个url添加到selector的circle中
        for(URL url : list){
            sel.add(url.toString());
        }
        //最后根据当前时间戳作为key，从circle中获取一个url
        long key = System.currentTimeMillis();
        String url = sel.get(key);
        //将字符串在list中找到并返回
        for(URL u : list){
            if(u.toString().equals(url)){
                return u;
            }
        }
        return null;//事实上这里不可能触发
    }

    private static final class selector{
        private final int numberOfReplicas;//虚拟节点数
        private final SortedMap<Long, String> circle = new TreeMap<Long, String>();//存储虚拟节点hash值与真实节点的映射关系

        public selector(int numberOfReplicas){
            this.numberOfReplicas = numberOfReplicas;
        }

        public void add(String node){//添加一个真实节点到circle中，同时生成对应的虚拟节点
            //对真实节点进行md5编码，得到一个字节数组
            byte[] digest = md5(node);
            //根据虚拟节点数，生成相应数量的hash值，并将其与真实节点映射存入circle中
            for(int i=0; i<numberOfReplicas; i++){
                long hash = hash(digest, i);
                circle.put(hash, node);
            }
        }

        public void remove(String node){//移除一个真实节点及其对应的虚拟节点
            //对真实节点进行md5编码，得到一个字节数组
            byte[] digest = md5(node);
            //根据虚拟节点数，生成相应数量的hash值，并将其从circle中删除
            for(int i=0; i<numberOfReplicas; i++){
                long hash = hash(digest, i);
                circle.remove(hash);
            }
        }

        public String get(long key){//根据一个key，从circle中获取一个真实节点
            //如果circle为空，返回null
            if(circle.isEmpty()){
                return null;
            }
            //根据key生成一个hash值
            long hash = hash(md5(String.valueOf(key)), 0);
            //如果circle中包含该hash值，直接返回对应的真实节点
            if(circle.containsKey(hash)){
                return circle.get(hash);
            }
            //否则，获取circle中大于该hash值的最小的hash值，返回对应的真实节点
            SortedMap<Long, String> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
            return circle.get(hash);
        }

        private long hash(byte[] digest, int nTime)//根据md5码，生成long类型的hash值
        {
            long rv = ((long) (digest[3 + nTime * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + nTime * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + nTime * 4] & 0xFF) << 8)
                    | (digest[0 + nTime * 4] & 0xFF);

            return rv & 0xffffffffL; /* Truncate to 32-bits */
        }
        private byte[] md5(String value)//传入字符串，获得md5码
        {
            MessageDigest md5 = null;
            try
            {
                md5 = MessageDigest.getInstance("MD5");
                md5.reset();
                md5.update(value.getBytes("UTF-8"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return md5.digest();
        }
    }
}
