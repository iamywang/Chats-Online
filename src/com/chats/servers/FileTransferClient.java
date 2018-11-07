package com.chats.servers;

import com.chats.frames.ChatForm;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.Socket;
import java.text.DecimalFormat;

public class FileTransferClient extends Socket implements Runnable {

    private static final String SERVER_IP = "127.0.0.1"; // �����IP
//	private static final int SERVER_PORT; // ����˶˿�

    private Socket client;

    private FileInputStream fis;

    private DataOutputStream dos;

    private static DecimalFormat df = null;

    static {
        // �������ָ�ʽ������һλ��ЧС��
        df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);
    }

    ChatForm owner;

    public FileTransferClient(ChatForm owner, int SERVER_PORT) throws Exception {
        super(SERVER_IP, SERVER_PORT);
        this.owner = owner;
        this.client = this;
        System.out.println("Client[port:" + client.getLocalPort() + "] �ɹ����ӷ����");

    }

    public void sendFile() throws IOException {
        try {
            JFileChooser f = new JFileChooser();
            f.showOpenDialog(null);
            File file = f.getSelectedFile().getAbsoluteFile();
            if (file.exists()) {
                fis = new FileInputStream(file);
                dos = new DataOutputStream(client.getOutputStream());

                // �ļ����ͳ���
                dos.writeUTF(file.getName());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();

                // ��ʼ�����ļ�
                System.out.println("======== ��ʼ�����ļ� ========");
                byte[] b = new byte[1024];
                long progress = 0;
                int read;
                while (-1 != (read = fis.read(b))) {
                    progress += read;
                    System.out.print("| " + (100 * progress / file.length()) + "% |");
                    dos.write(b, 0, read);
                    dos.flush();
                }
                System.out.println("======= �ļ�����ɹ� ========");

                owner.sendMessage(owner.getDateMessage() + "@FACE@" + "[�ļ�����ɹ���" + file.getAbsolutePath() + "]  [�ļ���С��"
                        + getFormatFileSize(file.length()) + "]");

            } else
                return;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null)
                fis.close();
            if (dos != null)
                dos.close();
            client.close();
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

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            sendFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
