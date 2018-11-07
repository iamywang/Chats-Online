package com.chats.database;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

public class SelectTable {
    private String url = "jdbc:mysql://localhost:3306/chats?verifyServerCertificate=false&useSSL=false";// JDBC��URL
    private String rootName = "root";
    private String pwd = "Yophs19990112";
    private String realpwd, realname, realsex, realmotto, imageurl;
    private int[] number;

    public int[] getAllNumbers() {
        number = new int[100];
        // 2.��������
        // ����DriverManager�����getConnection()���������һ��Connection����
        Connection conn = null;
        // ����Statement����
        Statement stmt = null;

        try {
            // �������ݿ�����
            conn = DriverManager.getConnection(url, rootName, pwd);
            stmt = conn.createStatement();// ����һ��Statement����

            // ���ݿ��ѯ���
            String sql = "select * from users";// Ҫִ�е�SQL

            /*
             * ��ѯ���ݱ�ʱ����Ҫ�õ�ResultSet�ӿڣ���������һ�����ݱ�ͨ���ýӿڵ�ʵ�� ���Ի�ü�����������Լ���Ӧ���ݱ�Ľӿ���Ϣ��
             */
            ResultSet rs = stmt.executeQuery(sql);// �������ݶ���
            int k = 0;

            // ������ѯ�Ľ����
            while (rs.next()) {
                number[k] = rs.getInt(1);
                realname = rs.getString(2);
                realpwd = rs.getString(3);
                realsex = rs.getString(4);
                realmotto = rs.getString(5);
                imageurl = rs.getString(6);
                k++;
            }
            // �ر�����
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public void selectExist() {

        // 2.��������
        // ����DriverManager�����getConnection()���������һ��Connection����
        Connection conn = null;
        // ����Statement����
        Statement stmt = null;

        try {
            // �������ݿ�����
            conn = DriverManager.getConnection(url, rootName, pwd);
            stmt = conn.createStatement();// ����һ��Statement����
            System.out.println("�ɹ����ӵ����ݿ�~");

            // ���ݿ��ѯ���
            String sql = "select * from users";// Ҫִ�е�SQL

            /*
             * ��ѯ���ݱ�ʱ����Ҫ�õ�ResultSet�ӿڣ���������һ�����ݱ�ͨ���ýӿڵ�ʵ�� ���Ի�ü�����������Լ���Ӧ���ݱ�Ľӿ���Ϣ��
             */
            ResultSet rs = stmt.executeQuery(sql);// �������ݶ���

            System.out.println("�� ��" + "\t" + "�� ��" + "\t" + "�� ��" + "\t" + "�� ��" + "\t" + "�� ǩ" + "\t" + "ͷ ��");
            // ������ѯ�Ľ����
            while (rs.next()) {
                System.out.print(rs.getInt(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getString(3) + "\t");
                System.out.print(rs.getString(4) + "\t");
                System.out.print(rs.getString(5) + "\t");
                System.out.print(rs.getString(6) + "\t");
                System.out.println();
            }
            // �ر�����
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int selectExist(long num) {

        // 2.��������
        // ����DriverManager�����getConnection()���������һ��Connection����
        Connection conn = null;
        // ����Statement����
        Statement stmt = null;

        try {
            // �������ݿ�����
            conn = DriverManager.getConnection(url, rootName, pwd);
            stmt = conn.createStatement();// ����һ��Statement����
            // ���ݿ��ѯ���
            String sql = "select * from users where number = " + num;// Ҫִ�е�SQL

            /*
             * ��ѯ���ݱ�ʱ����Ҫ�õ�ResultSet�ӿڣ���������һ�����ݱ�ͨ���ýӿڵ�ʵ�� ���Ի�ü�����������Լ���Ӧ���ݱ�Ľӿ���Ϣ��
             */
            ResultSet rs = stmt.executeQuery(sql);// �������ݶ���

            // ������ѯ�Ľ����
            while (rs.next()) {
                realname = rs.getString(2);
                realpwd = rs.getString(3);
                realsex = rs.getString(4);
                realmotto = rs.getString(5);
                imageurl = rs.getString(6);
                return 1;
            }
            // �ر�����
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