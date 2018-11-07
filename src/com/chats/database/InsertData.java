package com.chats.database;

import java.sql.*;

public class InsertData {
    private String url = "jdbc:mysql://localhost:3306/chats?verifyServerCertificate=false&useSSL=false";// JDBC的URL
    private String rootName = "root";
    private String pwd = "Yophs19990112";

    public void insertNew(int num, String name, String pswd, String sex, String motto, String image) {
        // 2.建立连接
        // 调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn = null;
        // 创建Statement对象
        Statement stmt = null;

        try {
            // 建立数据库连接
            conn = DriverManager.getConnection(url, rootName, pwd);
            stmt = conn.createStatement();// 创建一个Statement对象
            System.out.println("成功连接到数据库~");

            // 刷新的sql语句
            String sql = "insert into users values(" + num + ",'" + name + "','" + pswd + "','" + sex + "','" + motto
                    + "','" + image + "')";
            // 建立PreparedStatement对象
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            // System.out.println(pst);

            String sql3 = "select * from users";// 要执行的SQL

            /*
             * 在询数据表时，需要用到ResultSet接口，它类似于一个数据表，通过该接口的实例 可以获得检索结果集，以及对应数据表的接口信息。
             */
            ResultSet rs = stmt.executeQuery(sql3);// 创建数据对象

            System.out.println("号 码" + "\t" + "昵 称" + "\t" + "密 码" + "\t" + "性 别" + "\t" + "个 签" + "\t" + "头 像");
            // 遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getString(3) + "\t");
                System.out.print(rs.getString(4) + "\t");
                System.out.print(rs.getString(5) + "\t");
                System.out.print(rs.getString(6) + "\t");
                System.out.println();
            }

            // 关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}