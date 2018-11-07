package com.chats.guess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GuessServer {

    //�߳�����  
    public static ArrayList<ServerThread> list = new ArrayList<ServerThread>();
    //�����ݿ��л�ȡ�µ���Ϣ  
    public static String[] infos;

    public void initServer() {
        try {
            //��������������  
            @SuppressWarnings("resource")
            ServerSocket server = new ServerSocket(12345);
            System.out.println("�������Ѿ�����.......");
            while (true) {
                //ѭ�����տͻ��˵�����  
                Socket socket = server.accept();
                System.out.println("�ͻ���������......");
                //����һ���µ��̴߳���  
                ServerThread st = new ServerThread(socket);
                list.add(st);
                //���������ͻ����������ʱ����ʼ��Ϸ  
                if (list.size() == 2) {
                    GuessDataBase db = new GuessDataBase();
                    //�����ݿ��л�ȡһ����¼  
                    String guessinfo = db.getInfo();
                    infos = guessinfo.split("#");
                    //��һ��������ǻ�  
                    list.get(0).name = "draw";
                    //�ڶ�������������ǲ�  
                    list.get(1).name = "guess";
                    //����������������ǲ�  
//                  list.get(2).name="guess";  

                    list.get(0).start();
                    list.get(1).start();
//                  list.get(2).start();  
                    System.out.println("�����߳�......");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}  