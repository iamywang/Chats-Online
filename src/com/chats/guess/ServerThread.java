package com.chats.guess;

import java.io.*;
import java.net.Socket;


public class ServerThread extends Thread {

    public Socket socket;
    //�߳�����  
    public String name;
    //���������  
    public DataInputStream dis;
    public DataOutputStream dos;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            //��ȡ���������  
            InputStream ins = socket.getInputStream();
            OutputStream ous = socket.getOutputStream();
            //�����߳��Ժ󣬴����ݿ���ȡһ����Ϣ
            dis = new DataInputStream(ins);
            dos = new DataOutputStream(ous);
            //����ǻ��Ŀͻ���
            if ("draw".equals(name)) {
                //���ͻ�����Ϣ���ͻ���  
                sendMsg(socket.getOutputStream(), "draw");
//              System.out.println("�ѷ���draw");  
                //����Ҫ������Ϣ�����Ŀͻ���  
                dos.writeUTF(GuessServer.infos[0]);
                //ѭ���տͻ��˻�����Ϣ  
                while (true) {
                    int x1 = dis.readInt();
                    int y1 = dis.readInt();
                    int x2 = dis.readInt();
                    int y2 = dis.readInt();
                    int color = dis.readInt();
                    int width = dis.readInt();
                    //������Ϣת�����µĿͻ���
                    for (int i = 0; i < GuessServer.list.size(); i++) {
                        ServerThread st = GuessServer.list.get(i);
                        if (st != this) {
                            //����ͷ��������Ϣ
                            sendMsg(st.socket.getOutputStream(), "data");
                            //����������Ϣ
                            sendMsg1(st.socket.getOutputStream(), x1, y1, x2, y2, color, width);
                        }
                    }
                }
            }


            if ("guess".equals(name)) {
                //���Ͳ���Ϣ���ͻ���  
                sendMsg(ous, "guess");
                System.out.println("�ѷ���guess");
                //����ʾ��Ϣ���͸��µĿͻ���  
                dos.writeUTF(GuessServer.infos[1]);
                while (true) {
                    //���Ͻ��ղµĿͻ��˷��͹�������Ϣ  
                    String msg = dis.readUTF();

                    System.out.println("�������˽��ܵ���ϢΪ��" + msg);

                    //��ÿ���ͻ��˷�����Ϣͷ������  
                    for (int i = 0; i < GuessServer.list.size(); i++) {
                        ServerThread st = GuessServer.list.get(i);
                        sendMsg(st.socket.getOutputStream(), "msg");
                    }

                    //���ĳ�����̲߳¶���  
                    if (GuessServer.infos[0].equals(msg)) {

                        GuessServer.list.get(0).dos.writeUTF("yes");

                        this.dos.writeUTF("yes");

                        this.dos.writeUTF("��˵:" + msg);

                        System.out.println("�Ѿ�����yes");
                    } else {

                        for (int i = 0; i < GuessServer.list.size(); i++) {
                            ServerThread st = GuessServer.list.get(i);
                            if (st == this) {
                                st.dos.writeUTF("��˵:" + msg);
                            } else {
                                st.dos.writeUTF("��˵:" + msg);
                            }
                        }

                        System.out.println("���������ѷ�������" + msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ������Ϣ�ĺ���  
    public void sendMsg(OutputStream os, String s) throws IOException {

        // ��ͻ��������Ϣ  
        // System.out.println(s);  
        byte[] bytes = s.getBytes();
        os.write(bytes);
        os.write(13);
        os.write(10);
        os.flush();

    }

    // ��ȡ�ͻ����������ݵĺ���  
    public String readMsg(InputStream ins) throws Exception {
        // ��ȡ�ͻ��˵���Ϣ  
        int value = ins.read();
        // ��ȡ���� ��ȡ���س���13�����У�10��ʱֹͣ��  
        String str = "";
        while (value != 10) {
            // ����رտͻ���ʱ�᷵��-1ֵ  
            if (value == -1) {
                throw new Exception();
            }
            str = str + ((char) value);
            value = ins.read();
        }
        str = str.trim();
        return str;
    }

    // ��������ĺ���  
    public void sendMsg1(OutputStream os, int x1, int y1, int x2, int y2, int color, int width) throws IOException {

        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(x1);
        dos.writeInt(y1);
        dos.writeInt(x2);
        dos.writeInt(y2);
        dos.writeInt(color);
        dos.writeInt(width);
        dos.flush();
    }
}  
