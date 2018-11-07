package com.chats.servers;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

public class FileTransferServer extends ServerSocket {

//	private static final int SERVER_PORT = 23456; // ����˶�

    private static DecimalFormat df = null;

    static {
        // �������ָ�ʽ������һλ��ЧС��
        df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);
    }

    public FileTransferServer(int SERVER_PORT) throws Exception {
        super(SERVER_PORT);
    }

    public void load() {
        while (true) {
            try {
                Socket socket = this.accept();
                new Thread(new Task(socket)).start();
                break;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class Task implements Runnable {

        private Socket socket;

        private DataInputStream dis;

        private FileOutputStream fos;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                dis = new DataInputStream(socket.getInputStream());

                // �ļ����ͳ���
                String fileName = dis.readUTF();
                long fileLength = dis.readLong();
                JFileChooser jfc = new JFileChooser();
                jfc.showSaveDialog(null);
                File directory = jfc.getCurrentDirectory().getAbsoluteFile();
                if (!directory.exists()) {
                    directory.mkdir();
                }
                File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                fos = new FileOutputStream(file);

                // ��ʼ�����ļ�
                byte[] bytes = new byte[1024];
                int length = 0;
                while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                    fos.write(bytes, 0, length);
                    fos.flush();
                }
                System.out.println("======== �ļ����ճɹ� [File Name��" + fileName + "] [Size��" + getFormatFileSize(fileLength)
                        + "] ========");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                    if (dis != null)
                        dis.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFormatFileSize(long length) {
        double size = ((double) length) / (1 << 30);
        if (size >= 1) {
            return df.format(size) + "GB";
        }
        size = ((double) length) / (1 << 20);
        if (size >= 1) {
            return df.format(size) + "MB";
        }
        size = ((double) length) / (1 << 10);
        if (size >= 1) {
            return df.format(size) + "KB";
        }
        return length + "B";
    }
}