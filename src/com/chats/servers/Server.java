package com.chats.servers;

import com.chats.friendlists.User;
import com.chats.guess.GuessServer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class Server {

    private JFrame frame;
    private JTextField txt_message;
    private JTextField txt_max;
    private JTextField txt_port;
    private JButton btn_start;
    private JButton btn_stop;
    private JButton btn_send;
    private JPanel northPanel;
    private JPanel southPanel;
    private JScrollPane rightPanel;
    private JScrollPane leftPanel;
    private JSplitPane centerSplit;
    @SuppressWarnings("rawtypes")
    private JList userList;
    @SuppressWarnings("rawtypes")
    private DefaultListModel listModel;

    private ServerSocket serverSocket;
    private ServerThread serverThread;
    private ArrayList<ClientThread> clients;

    private boolean isStart = false;
    private JTextPane contentArea;
    private StyledDocument docChat;
    private JButton button_1;
    private JButton button_2;

    // ִ����Ϣ����
    public void send() throws BadLocationException {
        if (!isStart) {
            JOptionPane.showMessageDialog(frame, "��������δ����,���ܷ�����Ϣ��", "����", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (clients.size() == 0) {
            JOptionPane.showMessageDialog(frame, "û���û�����,���ܷ�����Ϣ��", "����", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long timemillis = System.currentTimeMillis();
        // ת��������ʾ��ʽ
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String message = "������ " + df.format(new Date(timemillis)) + "\n" + txt_message.getText().trim();

        if (message == null || message.equals("")) {
            JOptionPane.showMessageDialog(frame, "��Ϣ����Ϊ�գ�", "����", JOptionPane.ERROR_MESSAGE);
            return;
        }
        sendServerMessage(message);// Ⱥ����������Ϣ
        docChat = contentArea.getStyledDocument();
        docChat.insertString(docChat.getLength(), message + "\r\n", contentArea.getStyle("normal"));
        contentArea.setCaretPosition(docChat.getLength());
        txt_message.setText(null);
    }

    // ���췽��
    @SuppressWarnings({"unused", "rawtypes", "unchecked"})
    public Server() {
        contentArea = new JTextPane();
        contentArea.setFont(new Font("΢���ź�", Font.BOLD, 13));
        Style normal = contentArea.getStyledDocument().addStyle(null, null);
        frame = new JFrame("������");
        frame.setVisible(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(Server.class.getResource("/faces/icons/serversform.png")));
        txt_message = new JTextField();
        txt_message.setFont(new Font("΢���ź�", Font.BOLD, 13));
        txt_max = new JTextField("30");
        txt_max.setFont(new Font("΢���ź�", Font.BOLD, 18));
        btn_start = new JButton("\u542F\u52A8\u804A\u5929\u670D\u52A1\u5668");
        btn_start.setFont(new Font("΢���ź�", Font.BOLD, 17));
        btn_send = new JButton("����");
        btn_send.setFont(new Font("΢���ź�", Font.BOLD, 18));
        listModel = new DefaultListModel();
        userList = new JList(listModel);
        userList.setFont(new Font("΢���ź�", Font.BOLD, 17));

        southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new TitledBorder("д��Ϣ"));
        southPanel.add(txt_message, "Center");
        southPanel.add(btn_send, "East");
        leftPanel = new JScrollPane(userList);
        leftPanel.setBorder(new TitledBorder("�����û�"));

        rightPanel = new JScrollPane();
        rightPanel.setBorder(new TitledBorder("��Ϣ��ʾ��"));

        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);

        button_2 = new JButton("\u540C\u6B65\u5217\u8868");
        button_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // ������ǰ�����û���Ϣ
                if (clients.size() > 0) {
                    String temp = "";
                    for (int i = 0; i <= clients.size() - 1; i++) {
                        temp += (clients.get(i).getUser().getName() + "/" + clients.get(i).getUser().getIp() + "@"
                                + clients.get(i).getUser().getSex() + "@");
                    }
                    for (int i = 0; i <= clients.size() - 1; i++) {
                        clients.get(i).getWriter().println("USERLIST@" + clients.size() + "@" + temp);
                        clients.get(i).getWriter().flush();
                    }
                }
            }
        });
        button_2.setFont(new Font("΢���ź�", Font.BOLD, 17));
        leftPanel.setColumnHeaderView(button_2);

        rightPanel.setViewportView(contentArea);
        centerSplit.setDividerLocation(150);
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 6));
        JLabel label = new JLabel("��������");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("΢���ź�", Font.BOLD, 18));
        northPanel.add(label);
        northPanel.add(txt_max);
        northPanel.add(btn_start);
        btn_stop = new JButton("\u505C\u6B62\u804A\u5929\u670D\u52A1\u5668");
        btn_stop.setFont(new Font("΢���ź�", Font.BOLD, 17));
        btn_stop.setEnabled(false);
        northPanel.add(btn_stop);

        // ����ֹͣ��������ťʱ�¼�
        btn_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isStart) {
                    JOptionPane.showMessageDialog(frame, "��������δ����������ֹͣ��", "����", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    closeServer();
                    btn_start.setEnabled(true);
                    txt_max.setEnabled(true);
                    txt_port.setEnabled(true);
                    btn_stop.setEnabled(false);
                    docChat = contentArea.getStyledDocument();
                    docChat.insertString(docChat.getLength(), "�������ɹ�ֹͣ!\r\n", contentArea.getStyle("normal"));
                    contentArea.setCaretPosition(docChat.getLength());
                    JOptionPane.showMessageDialog(frame, "�������ɹ�ֹͣ��");
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(frame, "ֹͣ�����������쳣��", "����", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JLabel label_1 = new JLabel("�˿�");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setFont(new Font("΢���ź�", Font.BOLD, 18));
        northPanel.add(label_1);
        txt_port = new JTextField("33333");
        txt_port.setFont(new Font("΢���ź�", Font.BOLD, 18));
        northPanel.add(txt_port);
        northPanel.setBorder(new TitledBorder("������Ϣ"));

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(northPanel, "North");

        button_1 = new JButton("\u542F\u52A8\u4F60\u753B\u6211\u731C\u670D\u52A1\u5668");
        button_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                class Guess extends Thread {
                    public void run() {
                        GuessServer gssv = new GuessServer();
                        gssv.initServer();
                    }
                }
                Guess guess = new Guess();
                guess.start();
                button_1.setEnabled(false);
            }
        });
        button_1.setFont(new Font("΢���ź�", Font.BOLD, 17));
        northPanel.add(button_1);
        frame.getContentPane().add(centerSplit, "Center");
        frame.getContentPane().add(southPanel, "South");
        frame.setSize(800, 500);
        // frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());//����ȫ��
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
        frame.setVisible(true);

        // �رմ���ʱ�¼�
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isStart) {
                    closeServer();// �رշ�����
                }

            }
        });

        // �ı��򰴻س���ʱ�¼�
        txt_message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    send();
                } catch (BadLocationException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        // �������Ͱ�ťʱ�¼�
        btn_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    send();
                } catch (BadLocationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        // ����������������ťʱ�¼�
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStart) {
                    JOptionPane.showMessageDialog(frame, "�������Ѵ�������״̬����Ҫ�ظ�������", "����", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int max;
                int port;
                try {
                    try {
                        max = Integer.parseInt(txt_max.getText());
                    } catch (Exception e1) {
                        throw new Exception("��������Ϊ��������");
                    }
                    if (max <= 0) {
                        throw new Exception("��������Ϊ��������");
                    }
                    try {
                        port = Integer.parseInt(txt_port.getText());
                    } catch (Exception e1) {
                        throw new Exception("�˿ں�Ϊ��������");
                    }
                    if (port <= 0) {
                        throw new Exception("�˿ں� Ϊ��������");
                    }
                    serverStart(max, port);
                    docChat = contentArea.getStyledDocument();
                    docChat.insertString(docChat.getLength(), "�������ѳɹ�����!�������ޣ�" + max + ",�˿ڣ�" + port + "\r\n" + "\r\n",
                            contentArea.getStyle("normal"));
                    contentArea.setCaretPosition(docChat.getLength());
                    JOptionPane.showMessageDialog(frame, "�������ɹ�����!");
                    btn_start.setEnabled(false);
                    txt_max.setEnabled(false);
                    txt_port.setEnabled(false);
                    btn_stop.setEnabled(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(frame, exc.getMessage(), "����", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // ����������
    public void serverStart(int max, int port) throws java.net.BindException {
        try {
            clients = new ArrayList<ClientThread>();
            serverSocket = new ServerSocket(port);
            serverThread = new ServerThread(serverSocket, max);
            serverThread.start();
            isStart = true;
        } catch (BindException e) {
            isStart = false;
            throw new BindException("�˿ں��ѱ�ռ�ã��뻻һ����");
        } catch (Exception e1) {
            e1.printStackTrace();
            isStart = false;
            throw new BindException("�����������쳣��");
        }
    }

    // �رշ�����
    @SuppressWarnings("deprecation")
    public void closeServer() {
        try {
            if (serverThread != null)
                serverThread.stop();// ֹͣ�������߳�

            for (int i = clients.size() - 1; i >= 0; i--) {
                // �����������û����͹ر�����
                clients.get(i).getWriter().println("CLOSE");
                clients.get(i).getWriter().flush();
                // �ͷ���Դ
                clients.get(i).stop();// ֹͣ����Ϊ�ͻ��˷�����߳�
                clients.get(i).reader.close();
                clients.get(i).writer.close();
                clients.get(i).socket.close();
                clients.remove(i);
            }
            if (serverSocket != null) {
                serverSocket.close();// �رշ�����������
            }
            listModel.removeAllElements();// ����û��б�
            isStart = false;
        } catch (IOException e) {
            e.printStackTrace();
            isStart = true;
        }
    }

    // Ⱥ����������Ϣ
    public void sendServerMessage(String message) {
        for (int i = clients.size() - 1; i >= 0; i--) {
            clients.get(i).getWriter().println(message);
            clients.get(i).getWriter().flush();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @SuppressWarnings("unused")
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
                    Server server = new Server();
//					server.btn_start.doClick();;
//					server.button_1.doClick();
//					class timert extends TimerTask{
//						@Override
//						public void run() {
//							server.button_2.doClick();;
//						}
//					}
//					Timer timer = new Timer();
//					timer.schedule(new timert(),0,1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // �������߳�
    class ServerThread extends Thread {
        private ServerSocket serverSocket;
        private int max;// ��������

        // �������̵߳Ĺ��췽��
        public ServerThread(ServerSocket serverSocket, int max) {
            this.serverSocket = serverSocket;
            this.max = max;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            while (true) {// ��ͣ�ĵȴ��ͻ��˵�����
                try {
                    Socket socket = serverSocket.accept();
                    if (clients.size() == max) {// ����Ѵ���������
                        BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter w = new PrintWriter(socket.getOutputStream());
                        // ���տͻ��˵Ļ����û���Ϣ
                        String inf = r.readLine();
                        StringTokenizer st = new StringTokenizer(inf, "@");
                        User user = new User(st.nextToken(), st.nextToken(), st.nextToken());
                        // �������ӳɹ���Ϣ
                        w.println("MAX@���������Բ���" + user.getName() + user.getIp() + "�����������������Ѵ����ޣ����Ժ������ӣ�");
                        w.flush();
                        // �ͷ���Դ
                        r.close();
                        w.close();
                        socket.close();
                        continue;
                    }
                    ClientThread client = new ClientThread(socket);
                    client.start();// �����Դ˿ͻ��˷�����߳�
                    clients.add(client);
                    listModel.addElement(client.getUser().getName());// ���������б�
                    docChat = contentArea.getStyledDocument();
                    try {
                        docChat.insertString(docChat.getLength(),
                                client.getUser().getName() + client.getUser().getIp() + "����!\r\n",
                                contentArea.getStyle("normal"));
                    } catch (BadLocationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    contentArea.setCaretPosition(docChat.getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Ϊһ���ͻ��˷�����߳�
    class ClientThread extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private User user;

        public BufferedReader getReader() {
            return reader;
        }

        public PrintWriter getWriter() {
            return writer;
        }

        public User getUser() {
            return user;
        }

        // �ͻ����̵߳Ĺ��췽��
        public ClientThread(Socket socket) {
            try {
                this.socket = socket;
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
                // ���տͻ��˵Ļ����û���Ϣ
                String inf = reader.readLine();
                System.out.println(inf);
                StringTokenizer st = new StringTokenizer(inf, "@");
                user = new User(st.nextToken(), st.nextToken(), st.nextToken());
                // �������ӳɹ���Ϣ
                writer.println(user.getName() + user.getIp() + "����������ӳɹ�!");
                writer.flush();
                // ������ǰ�����û���Ϣ
                if (clients.size() > 0) {
                    String temp = "";
                    for (int i = clients.size() - 1; i >= 0; i--) {
                        temp += (clients.get(i).getUser().getName() + "/" + clients.get(i).getUser().getIp() + "@"
                                + clients.get(i).getUser().getSex() + "@");
                    }
                    writer.println("USERLIST@" + clients.size() + "@" + temp);
                    writer.flush();
                }
                // �����������û����͸��û���������
                for (int i = clients.size() - 1; i >= 0; i--) {
                    clients.get(i).getWriter().println("ADD@" + user.getName() + user.getIp() + user.getSex());
                    clients.get(i).getWriter().flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        @SuppressWarnings("deprecation")
        public void run() {// ���Ͻ��տͻ��˵���Ϣ�����д���
            String message = null;
            while (true) {
                try {
                    message = reader.readLine();// ���տͻ�����Ϣ
                    if (message.equals("CLOSE"))// ��������
                    {
                        docChat = contentArea.getStyledDocument();
                        try {
                            docChat.insertString(docChat.getLength(),
                                    this.getUser().getName() + this.getUser().getIp() + "����!\r\n",
                                    contentArea.getStyle("normal"));
                        } catch (BadLocationException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        contentArea.setCaretPosition(docChat.getLength());
                        // �Ͽ������ͷ���Դ
                        reader.close();
                        writer.close();
                        socket.close();

                        // �����������û����͸��û�����������
                        for (int i = clients.size() - 1; i >= 0; i--) {
                            clients.get(i).getWriter().println("DELETE@" + user.getName());
                            clients.get(i).getWriter().flush();
                        }

                        listModel.removeElement(user.getName());// ���������б�

                        // ɾ�������ͻ��˷����߳�
                        for (int i = clients.size() - 1; i >= 0; i--) {
                            if (clients.get(i).getUser() == user) {
                                ClientThread temp = clients.get(i);
                                clients.remove(i);// ɾ�����û��ķ����߳�
                                temp.stop();// ֹͣ���������߳�
                                return;
                            }
                        }
                    } else {
                        dispatcherMessage(message);// ת����Ϣ
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // ת����Ϣ
        public void dispatcherMessage(String message) {
            StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
            String source = stringTokenizer.nextToken();
            String owner = stringTokenizer.nextToken();
            String content = stringTokenizer.nextToken();
            message = source + "\n" + content;
            docChat = contentArea.getStyledDocument();
            try {
                docChat.insertString(docChat.getLength(), message + "\r\n", contentArea.getStyle("normal"));
            } catch (BadLocationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            contentArea.setCaretPosition(docChat.getLength());
            if (owner.equals("FACE")) {// Ⱥ��
                for (int i = clients.size() - 1; i >= 0; i--) {
                    clients.get(i).getWriter().println(message);
                    clients.get(i).getWriter().flush();
                }
            } else {// ����
                int num = Integer.parseInt(owner);
                clients.get(num).getWriter().println(message);
                clients.get(num).getWriter().flush();
            }
        }
    }
}
