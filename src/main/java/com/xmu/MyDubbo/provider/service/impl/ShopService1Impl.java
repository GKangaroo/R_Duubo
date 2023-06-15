package com.xmu.MyDubbo.provider.service.impl;

import com.xmu.MyDubbo.provider.service.ShopServiceInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShopService1Impl implements ShopServiceInterface {
    public static final String VERSION = "1";
    @Override
    public String getShop(String id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://192.168.254.2:3306/oomall";
            String username = "demouser";
            String password = "123456";

            Connection connection = DriverManager.getConnection(url, username, password);
            // 这里只是一个简单的调用示例，会有SQL注入的风险，不应用于正式业务代码
            String sql = "SELECT * FROM `shop` where id = "+id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            String res = "";
            while (resultSet.next()) {
                res += resultSet.getInt("id") + " " + resultSet.getString("name") + " " + resultSet.getTime("gmt_create");
            }
            return res;
        }
        catch (Exception ignored){
            ignored.printStackTrace();
            return "error";
        }
    }
    // 下面的代码是验证代码，下面的能跑，上面的函数也能跑
    public static void main(String[] args){
        System.out.println(new ShopService1Impl().getShop("1"));
    }
}
