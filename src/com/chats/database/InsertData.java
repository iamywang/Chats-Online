package com.chats.database;

import java.sql.*;

public class InsertData {
    private String url = "jdbc:mysql://localhost:3306/chats?verifyServerCertificate=false&useSSL=false";// JDBC��URL
    private String rootName = "root";
    private String pwd = "Yophs19990112";

    public void insertNew(int num, String name, String pswd, String sex, String motto, String image) {
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

            // ˢ�µ�sql���
            String sql = "insert into users values(" + num + ",'" + name + "','" + pswd + "','" + sex + "','" + motto
                    + "','" + image + "')";
            // ����PreparedStatement����
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            // System.out.println(pst);

            String sql3 = "select * from users";// Ҫִ�е�SQL

            /*
             * ��ѯ���ݱ�ʱ����Ҫ�õ�ResultSet�ӿڣ���������һ�����ݱ�ͨ���ýӿڵ�ʵ�� ���Ի�ü�����������Լ���Ӧ���ݱ�Ľӿ���Ϣ��
             */
            ResultSet rs = stmt.executeQuery(sql3);// �������ݶ���

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
}