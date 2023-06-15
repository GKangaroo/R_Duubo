package com.xmu.MyDubbo.framework.protocol.http;

import com.xmu.MyDubbo.framework.Invocation;
import com.xmu.MyDubbo.provider.LocalRegister;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

public class HttpServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    public void handler(HttpServletRequest req, HttpServletResponse resp) {


        try {
            // 从输入流中获取调用对象
            ServletInputStream reqInputStream = req.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(reqInputStream);
            Invocation invocation = (Invocation) objectInputStream.readObject();
            objectInputStream.close();
            logger.info("invocation:" + invocation);
            // 获取接口名
            String interfaceName = invocation.getInterfaceName();
            // 获取版本号
            String version = invocation.getVersion();
            // 获取实现类
            Class implClass = LocalRegister.get(interfaceName + version);
            // 通过 {方法名} 和 {参数类型列表} 唯一确定方法
            Method method = implClass.getMethod(invocation.getMethodName(), invocation.getParamType());
            // 反射执行方法
            Object result = method.invoke(implClass.getConstructor().newInstance(), invocation.getParams());
            logger.info("result:" + result);
            // 将结果返回
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
