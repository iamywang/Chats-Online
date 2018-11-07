package com.chats.database;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

public class SelectTable {
    private String url = "jdbc:mysql://localhost:3306/chats?verifyServerCertificate=false&useSSL=false";// JDBC的URL
    private String rootName = "root";
    private String pwd = "Yophs19990112";
    private String realpwd, realname, realsex, realmotto, imageurl;
    private int[] number;

    public int[] getAllNumbers() {
        number = new int[100];
        // 2.建立连接
        // 调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn = null;
        // 创建Statement对象
        Statement stmt = null;

        try {
            // 建立数据库连接
            conn = DriverManager.getConnection(url, rootName, pwd);
            stmt = conn.createStatement();// 创建一个Statement对象

            // 数据库查询语句
            String sql = "select * from users";// 要执行的SQL

            /*
             * 在询数据表时，需要用到ResultSet接口，它类似于一个数据表，通过该接口的实例 可以获得检索结果集，以及对应数据表的接口信息。
             */
            ResultSet rs = stmt.executeQuery(sql);// 创建数据对象
            int k = 0;

            // 遍历查询的结果集
            while (rs.next()) {
                number[k] = rs.getInt(1);
                realname = rs.getString(2);
                realpwd = rs.getString(3);
                realsex = rs.getString(4);
                realmotto = rs.getString(5);
                imageurl = rs.getString(6);
                k++;
            }
            // 关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public void selectExist() {

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

            // 数据库查询语句
            String sql = "select * from users";// 要执行的SQL

            /*
             * 在询数据表时，需要用到ResultSet接口，它类似于一个数据表，通过该接口的实例 可以获得检索结果集，以及对应数据表的接口信息。
             */
            ResultSet rs = stmt.executeQuery(sql);// 创建数据对象

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

    public int selectExist(long num) {

        // 2.建立连接
        // 调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn = null;
        // 创建Statement对象
        Statement stmt = null;

        try {
            // 建立数据库连接
            conn = DriverManager.getConnection(url, rootName, pwd);
            stmt = conn.createStatement();// 创建一个Statement对象
            // 数据库查询语句
            String sql = "select * from users where number = " + num;// 要执行的SQL

            /*
             * 在询数据表时，需要用到ResultSet接口，它类似于一个数据表，通过该接口的实例 可以获得检索结果集，以及对应数据表的接口信息。
             */
            ResultSet rs = stmt.executeQuery(sql);// 创建数据对象

            // 遍历查询的结果集
            while (rs.next()) {
                realname = rs.getString(2);
                realpwd = rs.getString(3);
                realsex = rs.getString(4);
                realmotto = rs.getString(5);
                imageurl = rs.getString(6);
                return 1;
            }
            // 关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {

        }
        return -1;
    }

    public String getRealpwd() {
        return realpwd;
    }

    public String getRealName() {
        return realname;
    }

    public String getRealSex() {
        return realsex;
    }

    public String getRealMotto() {
        return realmotto;
    }

    public ImageIcon getImage() {
        try {
            URL urlx = new URL(imageurl);
            ImageIcon icon = new ImageIcon(urlx);
            return icon;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

}