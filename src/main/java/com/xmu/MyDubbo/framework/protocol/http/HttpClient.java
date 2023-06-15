package com.xmu.MyDubbo.framework.protocol.http;

import com.xmu.MyDubbo.framework.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpClient {
    public static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public Object send(String hostname, Integer port, Invocation invocation) {

        try {

            URL url = new URL("http", hostname, port, "/");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            // 需要传输数据
            httpURLConnection.setDoOutput(true);
            // 获取输出流
            OutputStream outputStream = httpURLConnection.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(invocation);
            objectOutputStream.flush();
            objectOutputStream.close();

            logger.info("http request send to " + hostname + ":" + port);

            InputStream inputStream = httpURLConnection.getInputStream();

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            Object result = objectInputStream.readObject();
            objectInputStream.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
