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

    // 执行消息发送
    public void send() throws BadLocationException {
        if (!isStart) {
            JOptionPane.showMessageDialog(frame, "服务器还未启动,不能发送消息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (clients.size() == 0) {
            JOptionPane.showMessageDialog(frame, "没有用户在线,不能发送消息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long timemillis = System.currentTimeMillis();
        // 转换日期显示格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String message = "服务器 " + df.format(new Date(timemillis)) + "\n" + txt_message.getText().trim();

        if (message == null || message.equals("")) {
            JOptionPane.showMessageDialog(frame, "消息不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        sendServerMessage(message);// 群发服务器消息
        docChat = contentArea.getStyledDocument();
        docChat.insertString(docChat.getLength(), message + "\r\n", contentArea.getStyle("normal"));
        contentArea.setCaretPosition(docChat.getLength());
        txt_message.setText(null);
    }

    // 构造方法
    @SuppressWarnings({"unused", "rawtypes", "unchecked"})
    public Server() {
        contentArea = new JTextPane();
        contentArea.setFont(new Font("微软雅黑", Font.BOLD, 13));
        Style normal = contentArea.getStyledDocument().addStyle(null, null);
        frame = new JFrame("服务器");
        frame.setVisible(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(Server.class.getResource("/faces/icons/serversform.png")));
        txt_message = new JTextField();
        txt_message.setFont(new Font("微软雅黑", Font.BOLD, 13));
        txt_max = new JTextField("30");
        txt_max.setFont(new Font("微软雅黑", Font.BOLD, 18));
        btn_start = new JButton("\u542F\u52A8\u804A\u5929\u670D\u52A1\u5668");
        btn_start.setFont(new Font("微软雅黑", Font.BOLD, 17));
        btn_send = new JButton("发送");
        btn_send.setFont(new Font("微软雅黑", Font.BOLD, 18));
        listModel = new DefaultListModel();
        userList = new JList(listModel);
        userList.setFont(new Font("微软雅黑", Font.BOLD, 17));

        southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new TitledBorder("写消息"));
        southPanel.add(txt_message, "Center");
        southPanel.add(btn_send, "East");
        leftPanel = new JScrollPane(userList);
        leftPanel.setBorder(new TitledBorder("在线用户"));

        rightPanel = new JScrollPane();
        rightPanel.setBorder(new TitledBorder("消息显示区"));

        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);

        button_2 = new JButton("\u540C\u6B65\u5217\u8868");
        button_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // 反馈当前在线用户信息
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
        button_2.setFont(new Font("微软雅黑", Font.BOLD, 17));
        leftPanel.setColumnHeaderView(button_2);

        rightPanel.setViewportView(contentArea);
        centerSplit.setDividerLocation(150);
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(2, 6));
        JLabel label = new JLabel("人数上限");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("微软雅黑", Font.BOLD, 18));
        northPanel.add(label);
        northPanel.add(txt_max);
        northPanel.add(btn_start);
        btn_stop = new JButton("\u505C\u6B62\u804A\u5929\u670D\u52A1\u5668");
        btn_stop.setFont(new Font("微软雅黑", Font.BOLD, 17));
        btn_stop.setEnabled(false);
        northPanel.add(btn_stop);

        // 单击停止服务器按钮时事件
        btn_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isStart) {
                    JOptionPane.showMessageDialog(frame, "服务器还未启动，无需停止！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    closeServer();
                    btn_start.setEnabled(true);
                    txt_max.setEnabled(true);
                    txt_port.setEnabled(true);
                    btn_stop.setEnabled(false);
                    docChat = contentArea.getStyledDocument();
                    docChat.insertString(docChat.getLength(), "服务器成功停止!\r\n", contentArea.getStyle("normal"));
                    contentArea.setCaretPosition(docChat.getLength());
                    JOptionPane.showMessageDialog(frame, "服务器成功停止！");
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(frame, "停止服务器发生异常！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JLabel label_1 = new JLabel("端口");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setFont(new Font("微软雅黑", Font.BOLD, 18));
        northPanel.add(label_1);
        txt_port = new JTextField("33333");
        txt_port.setFont(new Font("微软雅黑", Font.BOLD, 18));
        northPanel.add(txt_port);
        northPanel.setBorder(new TitledBorder("配置信息"));

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
        button_1.setFont(new Font("微软雅黑", Font.BOLD, 17));
        northPanel.add(button_1);
        frame.getContentPane().add(centerSplit, "Center");
        frame.getContentPane().add(southPanel, "South");
        frame.setSize(800, 500);
        // frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());//设置全屏
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
        frame.setVisible(true);

        // 关闭窗口时事件
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isStart) {
                    closeServer();// 关闭服务器
                }

            }
        });

        // 文本框按回车键时事件
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

        // 单击发送按钮时事件
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

        // 单击启动服务器按钮时事件
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isStart) {
                    JOptionPane.showMessageDialog(frame, "服务器已处于启动状态，不要重复启动！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int max;
                int port;
                try {
                    try {
                        max = Integer.parseInt(txt_max.getText());
                    } catch (Exception e1) {
                        throw new Exception("人数上限为正整数！");
                    }
                    if (max <= 0) {
                        throw new Exception("人数上限为正整数！");
                    }
                    try {
                        port = Integer.parseInt(txt_port.getText());
                    } catch (Exception e1) {
                        throw new Exception("端口号为正整数！");
                    }
                    if (port <= 0) {
                        throw new Exception("端口号 为正整数！");
                    }
                    serverStart(max, port);
                    docChat = contentArea.getStyledDocument();
                    docChat.insertString(docChat.getLength(), "服务器已成功启动!人数上限：" + max + ",端口：" + port + "\r\n" + "\r\n",
                            contentArea.getStyle("normal"));
                    contentArea.setCaretPosition(docChat.getLength());
                    JOptionPane.showMessageDialog(frame, "服务器成功启动!");
                    btn_start.setEnabled(false);
                    txt_max.setEnabled(false);
                    txt_port.setEnabled(false);
                    btn_stop.setEnabled(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(frame, exc.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // 启动服务器
    public void serverStart(int max, int port) throws java.net.BindException {
        try {
            clients = new ArrayList<ClientThread>();
            serverSocket = new ServerSocket(port);
            serverThread = new ServerThread(serverSocket, max);
            serverThread.start();
            isStart = true;
        } catch (BindException e) {
            isStart = false;
            throw new BindException("端口号已被占用，请换一个！");
        } catch (Exception e1) {
            e1.printStackTrace();
            isStart = false;
            throw new BindException("启动服务器异常！");
        }
    }

    // 关闭服务器
    @SuppressWarnings("deprecation")
    public void closeServer() {
        try {
            if (serverThread != null)
                serverThread.stop();// 停止服务器线程

            for (int i = clients.size() - 1; i >= 0; i--) {
                // 给所有在线用户发送关闭命令
                clients.get(i).getWriter().println("CLOSE");
                clients.get(i).getWriter().flush();
                // 释放资源
                clients.get(i).stop();// 停止此条为客户端服务的线程
                clients.get(i).reader.close();
                clients.get(i).writer.close();
                clients.get(i).socket.close();
                clients.remove(i);
            }
            if (serverSocket != null) {
                serverSocket.close();// 关闭服务器端连接
            }
            listModel.removeAllElements();// 清空用户列表
            isStart = false;
        } catch (IOException e) {
            e.printStackTrace();
            isStart = true;
        }
    }

    // 群发服务器消息
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

    // 服务器线程
    class ServerThread extends Thread {
        private ServerSocket serverSocket;
        private int max;// 人数上限

        // 服务器线程的构造方法
        public ServerThread(ServerSocket serverSocket, int max) {
            this.serverSocket = serverSocket;
            this.max = max;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            while (true) {// 不停的等待客户端的链接
                try {
                    Socket socket = serverSocket.accept();
                    if (clients.size() == max) {// 如果已达人数上限
                        BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter w = new PrintWriter(socket.getOutputStream());
                        // 接收客户端的基本用户信息
                        String inf = r.readLine();
                        StringTokenizer st = new StringTokenizer(inf, "@");
                        User user = new User(st.nextToken(), st.nextToken(), st.nextToken());
                        // 反馈连接成功信息
                        w.println("MAX@服务器：对不起，" + user.getName() + user.getIp() + "，服务器在线人数已达上限，请稍后尝试连接！");
                        w.flush();
                        // 释放资源
                        r.close();
                        w.close();
                        socket.close();
                        continue;
                    }
                    ClientThread client = new ClientThread(socket);
                    client.start();// 开启对此客户端服务的线程
                    clients.add(client);
                    listModel.addElement(client.getUser().getName());// 更新在线列表
                    docChat = contentArea.getStyledDocument();
                    try {
                        docChat.insertString(docChat.getLength(),
                                client.getUser().getName() + client.getUser().getIp() + "上线!\r\n",
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

    // 为一个客户端服务的线程
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

        // 客户端线程的构造方法
        public ClientThread(Socket socket) {
            try {
                this.socket = socket;
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
                // 接收客户端的基本用户信息
                String inf = reader.readLine();
                System.out.println(inf);
                StringTokenizer st = new StringTokenizer(inf, "@");
                user = new User(st.nextToken(), st.nextToken(), st.nextToken());
                // 反馈连接成功信息
                writer.println(user.getName() + user.getIp() + "与服务器连接成功!");
                writer.flush();
                // 反馈当前在线用户信息
                if (clients.size() > 0) {
                    String temp = "";
                    for (int i = clients.size() - 1; i >= 0; i--) {
                        temp += (clients.get(i).getUser().getName() + "/" + clients.get(i).getUser().getIp() + "@"
                                + clients.get(i).getUser().getSex() + "@");
                    }
                    writer.println("USERLIST@" + clients.size() + "@" + temp);
                    writer.flush();
                }
                // 向所有在线用户发送该用户上线命令
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
        public void run() {// 不断接收客户端的消息，进行处理。
            String message = null;
            while (true) {
                try {
                    message = reader.readLine();// 接收客户端消息
                    if (message.equals("CLOSE"))// 下线命令
                    {
                        docChat = contentArea.getStyledDocument();
                        try {
                            docChat.insertString(docChat.getLength(),
                                    this.getUser().getName() + this.getUser().getIp() + "下线!\r\n",
                                    contentArea.getStyle("normal"));
                        } catch (BadLocationException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        contentArea.setCaretPosition(docChat.getLength());
                        // 断开连接释放资源
                        reader.close();
                        writer.close();
                        socket.close();

                        // 向所有在线用户发送该用户的下线命令
                        for (int i = clients.size() - 1; i >= 0; i--) {
                            clients.get(i).getWriter().println("DELETE@" + user.getName());
                            clients.get(i).getWriter().flush();
                        }

                        listModel.removeElement(user.getName());// 更新在线列表

                        // 删除此条客户端服务线程
                        for (int i = clients.size() - 1; i >= 0; i--) {
                            if (clients.get(i).getUser() == user) {
                                ClientThread temp = clients.get(i);
                                clients.remove(i);// 删除此用户的服务线程
                                temp.stop();// 停止这条服务线程
                                return;
                            }
                        }
                    } else {
                        dispatcherMessage(message);// 转发消息
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 转发消息
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
            if (owner.equals("FACE")) {// 群发
                for (int i = clients.size() - 1; i >= 0; i--) {
                    clients.get(i).getWriter().println(message);
                    clients.get(i).getWriter().flush();
                }
            } else {// 单发
                int num = Integer.parseInt(owner);
                clients.get(num).getWriter().println(message);
                clients.get(num).getWriter().flush();
            }
        }
    }
}
