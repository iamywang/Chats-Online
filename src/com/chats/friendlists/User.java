package com.chats.friendlists;

import com.chats.database.SelectTable;

import javax.swing.*;

//用户信息类  
public class User {
    private String name;
    private String ip;
    private String sex;
    private String motto;
    private int number;
    private ImageIcon image;
    private SelectTable seldata = new SelectTable();

    public User(String name, String ip, String sex) {
        this.name = name;
        this.ip = ip;
        this.sex = sex;
        String[] temp = name.split("（");
        temp = temp[1].split("）");
        number = Integer.parseInt(temp[0]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSex() {
        return sex;
    }

    public String getMotto() {
        seldata.selectExist(number);
        motto = seldata.getRealMotto();
        return motto;
    }

    public int getNumber() {
        return number;
    }

    public ImageIcon getImage() {
        seldata.selectExist(number);
        image = seldata.getImage();
        return image;
    }

}
