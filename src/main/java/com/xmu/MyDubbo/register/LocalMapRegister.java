package com.xmu.MyDubbo.register;

import com.xmu.MyDubbo.framework.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 桂庆薪
 */
public class LocalMapRegister implements Register {
    private static final Logger logger = LoggerFactory.getLogger(LocalMapRegister.class);
    private static Map<String, List<URL>> REGISTER = new HashMap<>();//存放服务名和URL

    // 注册有相应接口服务的服务器的URL
    @Override
    public void register(String interfaceName, URL url) {

        // 如果不存在为interfaceName的key就创建ArrayList，如果存在就返回原有的ArrayList，然后再添加url
        REGISTER.computeIfAbsent(interfaceName, k -> new ArrayList<URL>()).add(url);

        //上行代码中直接获取arraylist的引用进行add，因为ArrayList是引用因此REGISTER中也会相应更新，不用再调用put了
//        REGISTER.put(interfaceName, list);//这段不能少，否则注册就没意义了
        saveFile();

        //用文本存？

    }

    @Override
    public void unregister(String interfaceName, URL url) {

        List<URL> list = REGISTER.get(interfaceName);

        list.remove(url);
        // 此行同样没必要，remove后REGISTER中相应的value已经改变了，不用在put
//        REGISTER.put(interfaceName, list);

    }

    // 获取有提供接口名对应的服务器的列表
    @Override
    public List<URL> getURLList(String interfaceName) {//获得服务名对应的url名

        REGISTER = getFile();

        return REGISTER.get(interfaceName);

    }


    private static void saveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/com/xmu/MyDubbo/tempFile/temp.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(REGISTER);
            File f = new File("src/main/java/com/xmu/MyDubbo/tempFile/temp2.txt");
            if (!f.exists()) { // 判断文件是否存在
                f.createNewFile(); // 如果不存在，就创建一个新文件
            }
            FileWriter fw = new FileWriter(f);
            fw.write(REGISTER.toString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<URL>> getFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/java/com/xmu/MyDubbo/tempFile/temp.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Map<String, List<URL>> register = (Map<String, List<URL>>) objectInputStream.readObject();
            return register;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
