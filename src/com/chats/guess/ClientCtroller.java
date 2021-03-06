package com.chats.guess;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;


public class ClientCtroller {

    public Socket socket;
    public GuessForm ui;

    public DataInputStream dis;
    public DataOutputStream dos;

    //  public ClientCtroller(Socket socket) {
//      this.socket=socket;  
//  }  
//    
    public ClientCtroller(Socket socket, JFrame ui) {
        this.socket = socket;
        this.ui = (GuessForm) ui;
    }

    public void dealwith() {
        try {
            InputStream ins = this.socket.getInputStream();
            OutputStream ous = this.socket.getOutputStream();
            dis = new DataInputStream(ins);
            dos = new DataOutputStream(ous);
            String msg = readMsg(socket.getInputStream());

            if ("draw".equals(msg)) {
                // 如果是draw，则客户端显示画的界面  
                ui.remove(ui.waitPanel);
                ui.addDrawPanel();
                ui.sendBtn.setEnabled(false);
//                ui.repaint();
//              System.out.println("draw界面完毕比");  
                //接收要画的信息  
                String drawinfo = dis.readUTF();
                //将要画的信息添加到面板上  
                ui.contant.setText("画的东西为：" + drawinfo);

                while (true) {
                    String s = readMsg(ins);
                    System.out.println("draw 接受到的信息：" + s);
//                  System.out.println(s);  
                    //接收服务器端发送的猜的客户端猜的消息  
                    if (!"data".equals(s)) {
                        String s1 = dis.readUTF();
                        System.out.println("s1：" + s);
                        //如果猜对了，则在画的客户端显示猜对了  
                        if ("yes".equals(s1)) {
//                          JOptionPane.showMessageDialog(null, "你猜对了！");  
                            ui.jta.append("对方猜对了" + "\r\n");
                        }
                        //否则画的客户端显示猜的客户单端的消息
                        else {
                            ui.jta.append(s1 + "\r\n");
                        }
                    }
                }
            }
            if ("guess".equals(msg)) {
                // 如果是猜，则显示猜的客户端  
                String guessinfo = dis.readUTF();
                ui.remove(ui.waitPanel);
                ui.addDrawPanel();
                ui.addGuessPanel();
                ui.contant.setText(guessinfo);
                ui.sendBtn.setEnabled(true);
//              ui.repaint();  
//              System.out.println("guess界面完毕比");  
                while (true) {
                    //接收服务器端的数据  
                    String info = readMsg(ins);
                    //如果是数据消息  
//                  System.out.println("猜客户端接收的消息为:"+info);  
                    if ("data".equals(info)) {
                        readMsg1(socket.getInputStream());
                    }
                    //如果是自己发送的猜的消息  
                    else if ("msg".equals(info)) {
                        //再次读取一条消息  
                        String info2 = dis.readUTF();
                        System.out.println("info2" + info2);
                        if ("yes".equals(info2)) {
                            info2 = dis.readUTF();
                            ui.jta.append(info2 + "\r\n");
                            ui.jta.append("恭喜你猜对了......");
                        } else {
                            System.out.println("接收到的信息为：" + info2);
                            ui.jta.append(info2 + "\r\n");
                        }
                    }
//                      else{  
//                      String info3=dis.readUTF();  
//                  }  
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 发送消息的函数  
    public void sendMsg(OutputStream os, String s) throws IOException {

        // 向客户端输出信息  
        //  
        // System.out.println(s);  
        byte[] bytes = s.getBytes();
        os.write(bytes);
        os.write(13);
        os.write(10);
        os.flush();

    }

    // 读取客户端输入数据的函数  
    public String readMsg(InputStream ins) throws Exception {
        // 读取客户端的信息  
        int value = ins.read();
        // 读取整行 读取到回车（13）换行（10）时停止读  
        String str = "";
        while (value != 10) {
            // 点击关闭客户端时会返回-1值  
            if (value == -1) {
                throw new Exception();
            }
            str = str + ((char) value);
            value = ins.read();
        }
        str = str.trim();
        return str;
    }

    // 发送消息的函数  
    public void sendMsg1(OutputStream os, int x1, int y1, int x2, int y2, int color, int width) throws IOException {

        DataOutputStream dos = new DataOutputStream(os);
//      dos.writeUTF("data");  
        dos.writeInt(x1);
        dos.writeInt(y1);
        dos.writeInt(x2);
        dos.writeInt(y2);
        dos.writeInt(color);
        dos.writeInt(width);
        dos.flush();

    }

    public void readMsg1(InputStream is) throws IOException {

        DataInputStream dis = new DataInputStream(is);
        int x1 = dis.readInt();
        int y1 = dis.readInt();
        int x2 = dis.readInt();
        int y2 = dis.readInt();
        int color = dis.readInt();
        int width = dis.readInt();
        Color c = new Color(color);
        BasicStroke strock = new BasicStroke(width);
        ui.g.setColor(c);
        ui.g.setStroke(strock);
//      System.out.println("ui:"+ui);  
        ui.g.drawLine(x1, y1, x2, y2);
//      System.out.println("x1:"+x1+"y1:"+y1+"x2:"+x2+"y2:"+y2);  

    }

}  
