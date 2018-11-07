package com.chats.frames;

import com.chats.database.SelectTable;
import com.chats.faces.ChatPic;
import com.chats.faces.FacesWindow;
import com.chats.faces.ScreenShot;
import com.chats.friendlists.ChatsTrayIcon;
import com.chats.friendlists.ImageCellRender;
import com.chats.friendlists.ImageListModel;
import com.chats.friendlists.User;
import com.chats.guess.GuessForm;
import com.chats.servers.FileTransferClient;
import com.chats.servers.FileTransferServer;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatForm extends JFrame {

    private static final long serialVersionUID = 1L;
    public Socket socket;
    public PrintWriter writer;
    public BufferedReader reader;
    public MessageThread messageThread;// 负责接收消息的线程
    private Map<String, User> onLineUsers = new HashMap<String, User>();// 所有在线用户
    private boolean isConnected = false;

    private String username, usernick, usersex;
    private JPanel contentPane, northPanel, westPanel, centerPanel, southp, mainPanel, userinfoPanel, panel, panel_1,
            panel_2, panel_3, panel_4, panel_5, panel_6, panel_7, panel_8;
    private JLabel TipLabel, info, label, label_1, label_2, label_3, label_4;
    private JScrollPane scrollPane, scrollPane_1, scrollPane_2, scrollPane_3;
    private JButton EmojiTrans, FileTrans, MsgTrans, DrawTrans, PicTrans, FontChangeButton, connectServerBTN, button, button_4, button_5, button_6, button_7, btnNewButton, btnNewButton_1;
    @SuppressWarnings("rawtypes")
    private JList list_1 = new JList();
    @SuppressWarnings("rawtypes")
    private JList list_2 = new JList();
    private ImageListModel listModel, listModel_2;
    private StyledDocument docChat = null;
    private CardLayout center = new CardLayout(0, 0);
    private JTextPane jpChat, MsgBox;
    private JComboBox<String> comboBox;
    int k = 0;

    private FileTransferClient ft;
    private FileTransferServer ftsv;
    private NewMessageBox tipbox = new NewMessageBox();
    private FacesWindow picWindow = new FacesWindow(this);
    private JButton button_8;
    private JButton button_9;
    private JButton button_10;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ChatForm(String usernick, String username, int port, String usersex, ImageIcon image) throws Exception {
        this.username = username;
        this.usernick = usernick;
        this.usersex = usersex;
        setIconImage(Toolkit.getDefaultToolkit().getImage(ChatForm.class.getResource("/faces/icons/chatform.png")));
        setBackground(Color.WHITE);
        setTitle(usernick + "（" + username + "）");
        setBounds(0, 0, 1050, 600);
        this.setLocationRelativeTo(null);
        contentPane = new JPanel();
        setContentPane(contentPane);

        northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        northPanel.setLayout(new BorderLayout(0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));

        westPanel = new JPanel();
        westPanel.setBackground(Color.WHITE);
        contentPane.add(westPanel, BorderLayout.WEST);
        CardLayout west = new CardLayout(0, 0);
        westPanel.setLayout(west);

        scrollPane_1 = new JScrollPane();
        westPanel.add(scrollPane_1, "name_1742118629646");
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.setBackground(Color.WHITE);

        listModel = new ImageListModel();
        listModel_2 = new ImageListModel();
        list_2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (list_2.getSelectedIndex() != -1) {
                    center.first(centerPanel);
                    User user = listModel_2.getElementAt(list_2.getSelectedIndex());
                    info.setIcon(user.getImage());
                    info.setText("<html><b><font style = color:red>" + user.getName()
                            + "</b><br><font style = color:gray>在线状态：<font style = color:black>"
                            + comboBox.getSelectedItem().toString()
                            + "<br><font style = color:gray>服务器地址：<font style = color:black>" + user.getIp()
                            + "<br><font style = color:gray>号码：<font style = color:black>" + user.getNumber()
                            + "<br><font style = color:gray>性别：<font style = color:black>" + user.getSex()
                            + "<br><font style = color:gray>年龄：<font style = color:black>19"
                            + "<br><font style = color:gray>分组：<font style = color:black>我的好友"
                            + "<br><font style = color:gray>所在地：<font style = color:black>山东省济南市"
                            + "<br><font style = color:gray>个性签名：<font style = color:black>" + user.getMotto()
                            + "<br><font style = color:gray>注册时间：<font style = color:black>2018年5月");
                }
            }
        });
        list_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list_2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        list_2.setBackground(Color.WHITE);
        list_2.setModel(listModel_2);
        list_2.setCellRenderer(new ImageCellRender());
        scrollPane_1.setViewportView(list_2);

        panel_6 = new JPanel();
        panel_6.setBackground(Color.WHITE);
        scrollPane_1.setColumnHeaderView(panel_6);
        panel_6.setLayout(new BorderLayout(0, 0));

        label = new JLabel("我的好友");
        panel_6.add(label, BorderLayout.CENTER);
        label.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/talk.png")));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(new Color(0, 128, 0));
        label.setFont(new Font("微软雅黑", Font.BOLD, 17));
        label.setBackground(Color.WHITE);

        button = new JButton("添加");
        button.setForeground(new Color(0, 255, 255));
        button.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        panel_6.add(button, BorderLayout.EAST);
        button.setBackground(new Color(0, 0, 128));
        button.setToolTipText("");

        scrollPane_2 = new JScrollPane();
        westPanel.add(scrollPane_2, "name_1742133191275");
        scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_2.setBackground(Color.WHITE);
        list_1.setModel(listModel);
        list_1.setCellRenderer(new ImageCellRender());
        scrollPane_2.setViewportView(list_1);
        list_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != 1)
                    list_1.setSelectedIndices(new int[]{});
            }
        });
        list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list_1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                jpChat.setText("");
            }
        });
        list_1.setBackground(Color.WHITE);
        list_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        label_3 = new JLabel("当前会话");
        label_3.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/talk.png")));
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        label_3.setForeground(new Color(153, 51, 0));
        label_3.setFont(new Font("微软雅黑", Font.BOLD, 17));
        label_3.setBackground(Color.WHITE);
        label_3.setOpaque(true);
        scrollPane_2.setColumnHeaderView(label_3);
        contentPane.add(northPanel, BorderLayout.NORTH);

        panel_2 = new JPanel();
        panel_2.setBackground(Color.WHITE);
        northPanel.add(panel_2, BorderLayout.WEST);
        panel_2.setLayout(new BorderLayout(0, 0));
        TipLabel = new JLabel();
        panel_2.add(TipLabel, BorderLayout.WEST);
        TipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TipLabel.setLabelFor(northPanel);
        TipLabel.setBackground(Color.WHITE);
        TipLabel.setIcon(image);

        panel_4 = new JPanel();
        panel_4.setBackground(Color.WHITE);
        panel_2.add(panel_4, BorderLayout.CENTER);

        label_2 = new JLabel("<html><body><p style=\"font-family:微软雅黑;font-size:16;color:blue\"><b>" + usernick
                + "<font style=\"font-family:黑体;font-size:16;color:black\">（" + username + "）");
        label_2.setForeground(Color.BLACK);
        label_2.setBackground(Color.WHITE);
        panel_4.setLayout(new BorderLayout(0, 0));

        panel_5 = new JPanel();
        panel_5.setBackground(Color.WHITE);
        panel_4.add(panel_5, BorderLayout.SOUTH);
        panel_5.setLayout(new BorderLayout(0, 0));

        comboBox = new JComboBox<String>();
        panel_5.add(comboBox, BorderLayout.WEST);
        comboBox.setEnabled(false);
        comboBox.setMaximumRowCount(2);
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"在线", "离线", "忙碌", "勿扰"}));
        comboBox.setSelectedIndex(1);
        comboBox.setFont(new Font("微软雅黑", Font.BOLD, 15));
        comboBox.setBackground(Color.WHITE);

        connectServerBTN = new JButton("连接");
        connectServerBTN.setForeground(new Color(0, 128, 128));
        connectServerBTN.setBackground(new Color(255, 255, 255));
        panel_5.add(connectServerBTN, BorderLayout.CENTER);
        connectServerBTN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    connectServer(port, "127.0.0.1", usernick + "（" + username + "）");
                } catch (BadLocationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        connectServerBTN.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        connectServerBTN.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/connectserver.png")));
        panel_4.add(label_2, BorderLayout.CENTER);

        panel_7 = new JPanel();
        panel_7.setBackground(Color.WHITE);
        northPanel.add(panel_7, BorderLayout.CENTER);
        panel_7.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btnNewButton = new JButton("");
        btnNewButton.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/userlist.png")));
        btnNewButton.setBackground(new Color(30, 144, 255));
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                west.first(westPanel);
                center.first(centerPanel);
            }
        });
        panel_7.add(btnNewButton);

        button_4 = new JButton("");
        button_4.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/chatlist.png")));
        button_4.setBackground(new Color(255, 0, 255));
        button_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                west.last(westPanel);
                center.last(centerPanel);
            }
        });
        panel_7.add(button_4);

        button_6 = new JButton("");
        button_6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                AboutForm abf = new AboutForm();
                abf.setVisible(true);
            }
        });
        button_6.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/settings.png")));
        button_6.setBackground(new Color(0, 255, 127));
        panel_7.add(button_6);

        centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(center);

        userinfoPanel = new JPanel();
        userinfoPanel.setBackground(Color.WHITE);
        centerPanel.add(userinfoPanel, "name_2857560547642");

        panel_3 = new JPanel();
        panel_3.setBackground(new Color(240, 248, 255));
        panel_3.setLayout(new BorderLayout(0, 0));
        info = new JLabel();
        info.setIcon(image);
        User user = new User(usernick + "（" + username + "）", "127.0.0.1", usersex);
        info.setText("<html><b><font style = color:red>" + user.getName()
                + "</b><br><font style = color:gray>在线状态：<font style = color:black>" + comboBox.getSelectedItem().toString()
                + "<br><font style = color:gray>服务器地址：<font style = color:black>" + user.getIp()
                + "<br><font style = color:gray>号码：<font style = color:black>" + user.getNumber()
                + "<br><font style = color:gray>性别：<font style = color:black>" + user.getSex()
                + "<br><font style = color:gray>年龄：<font style = color:black>19"
                + "<br><font style = color:gray>分组：<font style = color:black>我的好友"
                + "<br><font style = color:gray>所在地：<font style = color:black>山东省济南市"
                + "<br><font style = color:gray>个性签名：<font style = color:black>" + user.getMotto()
                + "<br><font style = color:gray>注册时间：<font style = color:black>2018年5月");
        panel_3.add(info);
        info.setBackground(Color.WHITE);
        info.setHorizontalAlignment(SwingConstants.CENTER);
        info.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        panel_8 = new JPanel();
        panel_8.setBackground(new Color(240, 248, 255));
        panel_3.add(panel_8, BorderLayout.SOUTH);

        btnNewButton_1 = new JButton("\u53D1\u9001\u6D88\u606F");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        btnNewButton_1.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/send.png")));
        panel_8.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        button_7 = new JButton("\u53D1\u8D77\u8BED\u97F3");
        button_7.setIcon(new ImageIcon(ChatForm.class.getResource("/com/chats/faces/photos/telephone.png")));
        button_7.setForeground(Color.BLACK);
        button_7.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        button_7.setBackground(new Color(135, 206, 250));
        panel_8.add(button_7);

        button_5 = new JButton("\u53D1\u9001\u6587\u4EF6");
        button_5.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/file.png")));
        button_5.setForeground(Color.BLACK);
        button_5.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        button_5.setBackground(new Color(135, 206, 250));
        panel_8.add(button_5);
        panel_8.add(btnNewButton_1);
        btnNewButton_1.setForeground(new Color(0, 0, 0));
        btnNewButton_1.setBackground(new Color(135, 206, 250));

        label_4 = new JLabel("\u7528\u6237\u4FE1\u606F");
        panel_3.add(label_4, BorderLayout.NORTH);
        label_4.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/user.png")));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setFont(new Font("微软雅黑", Font.BOLD, 18));
        GroupLayout gl_userinfoPanel = new GroupLayout(userinfoPanel);
        gl_userinfoPanel.setHorizontalGroup(gl_userinfoPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_userinfoPanel.createSequentialGroup().addGap(182)
                        .addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addGap(186)));
        gl_userinfoPanel.setVerticalGroup(gl_userinfoPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_userinfoPanel.createSequentialGroup().addGap(24)
                        .addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE).addGap(26)));
        userinfoPanel.setLayout(gl_userinfoPanel);

        mainPanel = new JPanel();
        centerPanel.add(mainPanel, "name_2788674737087");
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout(0, 0));

        scrollPane = new JScrollPane();
        mainPanel.add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.WHITE);

        jpChat = new JTextPane();
        scrollPane.setViewportView(jpChat);
        jpChat.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        jpChat.setForeground(Color.BLACK);
        jpChat.setEditable(false);

        label_1 = new JLabel("\u6D88\u606F\u5217\u8868");
        label_1.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/talk.png")));
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setForeground(new Color(0, 0, 139));
        label_1.setFont(new Font("微软雅黑", Font.BOLD, 17));
        label_1.setBackground(Color.WHITE);
        label_1.setOpaque(true);
        scrollPane.setColumnHeaderView(label_1);

        southp = new JPanel();
        southp.setBackground(Color.WHITE);
        mainPanel.add(southp, BorderLayout.SOUTH);
        southp.setLayout(new BorderLayout(0, 0));

        panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        southp.add(panel_1, BorderLayout.NORTH);
        DrawTrans = new JButton("");
        DrawTrans.setBackground(Color.WHITE);
        DrawTrans.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                class Guess extends Thread {
                    public void run() {
                        GuessForm form = new GuessForm();
//						form.initFrame();
                    }
                }
                Guess guess = new Guess();
                guess.start();
            }
        });
        DrawTrans.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/doubt.png")));
        DrawTrans.setToolTipText("\u4F60\u753B\u6211\u731C");
        FileTrans = new JButton("");
        FileTrans.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ft = new FileTransferClient(ChatForm.this, 20000 + list_1.getSelectedIndex());
                    Thread fff = new Thread(ft);
                    fff.start();

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        FileTrans.setBackground(Color.WHITE);
        FileTrans.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/file.png")));
        FileTrans.setToolTipText("\u6587\u4EF6\u4F20\u8F93");
        PicTrans = new JButton("");
        PicTrans.setBackground(Color.WHITE);
        PicTrans.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/pics.png")));
        PicTrans.setToolTipText("\u56FE\u7247");
        PicTrans.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                // 点击了发送图片按钮
                JFileChooser jfc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("图片(支持 bmp,jpg,png,gif 格式)", "bmp", "jpg",
                        "png", "gif");
                jfc.setFileFilter(filter);

                int rtn = jfc.showOpenDialog(jpChat);
                if (rtn == JFileChooser.APPROVE_OPTION) {
                    try {
                        String filePath = "file:/" + jfc.getSelectedFile().getAbsolutePath();
                        URL url = new URL(filePath);
                        // 向MsgBox中插入一张图片
                        ChatPic img = new ChatPic(url, 0);
                        MsgBox.insertIcon(img);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        EmojiTrans = new JButton("");
        EmojiTrans.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!picWindow.isVisible()) {
                    picWindow.setVisible(true);
                } else
                    picWindow.setVisible(false);
            }
        });
        EmojiTrans.setBackground(Color.WHITE);

        FontChangeButton = new JButton("");
        FontChangeButton.setBackground(Color.WHITE);
        FontChangeButton.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/fontbold.png")));
        FontChangeButton.setFont(new Font("微软雅黑", Font.BOLD, 17));
        EmojiTrans.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/smile.png")));
        EmojiTrans.setToolTipText("\u8868\u60C5");
        EmojiTrans.setFont(new Font("微软雅黑", Font.BOLD, 17));
        PicTrans.setFont(new Font("微软雅黑", Font.BOLD, 17));
        FileTrans.setFont(new Font("微软雅黑", Font.BOLD, 17));
        DrawTrans.setFont(new Font("微软雅黑", Font.BOLD, 17));

        button_9 = new JButton("");
        button_9.setIcon(new ImageIcon(ChatForm.class.getResource("/com/chats/faces/photos/infile.png")));
        button_9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ftsv.load();
            }
        });
        button_9.setToolTipText("\u6587\u4EF6\u63A5\u6536");
        button_9.setFont(new Font("微软雅黑", Font.BOLD, 17));
        button_9.setBackground(Color.WHITE);

        button_8 = new JButton("\u6E05\u5C4F");
        button_8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                jpChat.setText("");
            }
        });
        button_8.setForeground(new Color(240, 248, 255));
        button_8.setToolTipText("");
        button_8.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        button_8.setBackground(new Color(0, 0, 0));

        button_10 = new JButton("");
        button_10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // 自动生成截图名称
                String filename = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date()) + ".png";
                try {
                    // 调用截图
                    new ScreenShot(0, filename);
                } catch (AWTException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        button_10.setIcon(new ImageIcon(ChatForm.class.getResource("/com/chats/faces/photos/picshot.png")));
        button_10.setToolTipText("\u622A\u56FE");
        button_10.setFont(new Font("微软雅黑", Font.BOLD, 17));
        button_10.setBackground(Color.WHITE);
        GroupLayout gl_panel_1 = new GroupLayout(panel_1);
        gl_panel_1.setHorizontalGroup(
                gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel_1.createSequentialGroup()
                                .addGap(4)
                                .addComponent(FontChangeButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addComponent(EmojiTrans, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addComponent(PicTrans, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addComponent(button_10, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addComponent(FileTrans, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addComponent(button_9, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addComponent(DrawTrans, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED, 336, Short.MAX_VALUE)
                                .addComponent(button_8, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addGap(4))
        );
        gl_panel_1.setVerticalGroup(
                gl_panel_1.createParallelGroup(Alignment.LEADING)
                        .addComponent(FontChangeButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(EmojiTrans, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(PicTrans, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_10, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(FileTrans, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_9, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(DrawTrans, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl_panel_1.createSequentialGroup()
                                .addGap(4, 4, Short.MAX_VALUE)
                                .addComponent(button_8, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addGap(4))
        );
        panel_1.setLayout(gl_panel_1);

        scrollPane_3 = new JScrollPane();
        scrollPane_3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        southp.add(scrollPane_3, BorderLayout.CENTER);
        scrollPane_3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_3.setBackground(Color.WHITE);
        MsgBox = new JTextPane();
        scrollPane_3.setViewportView(MsgBox);
        MsgBox.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        southp.add(panel, BorderLayout.EAST);
        panel.setLayout(new BorderLayout(0, 0));
        MsgTrans = new JButton("");
        MsgTrans.setBackground(Color.WHITE);
        MsgTrans.setToolTipText("\u53D1\u9001\u6D88\u606F");
        panel.add(MsgTrans, BorderLayout.SOUTH);
        MsgTrans.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!isConnected) {
                    JOptionPane.showMessageDialog(null, "还没有连接服务器，无法发送消息！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (MsgBox.getText() == null || MsgBox.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "消息不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (list_1.isSelectionEmpty()) {
                    try {
                        sendMessage(getOutMsg("FACE"));
                    } catch (BadLocationException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                } else {
                    try {
                        String message = getOutMsg("" + list_1.getSelectedIndex());
                        sendMessage(message);
                        StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
                        String source = stringTokenizer.nextToken();
                        @SuppressWarnings("unused")
                        String owner = stringTokenizer.nextToken();
                        String content = stringTokenizer.nextToken();
                        message = source + "\n" + content;
                        String[] msggg = message.split("#");
                        for (int i = 0; i < msggg.length; i++) {
                            if (i % 2 == 0) {
                                docChat = jpChat.getStyledDocument();
                                docChat.insertString(docChat.getLength(), msggg[i], MsgBox.getStyle("normal"));
                                jpChat.setCaretPosition(docChat.getLength());
                            } else {
                                URL url = new URL(msggg[i]);
                                ImageIcon x = new ImageIcon(url);
                                jpChat.insertIcon(x);
                            }
                        }
                        docChat.insertString(docChat.getLength(), "\r\n", MsgBox.getStyle("normal"));
                        jpChat.setCaretPosition(docChat.getLength());
                    } catch (BadLocationException | MalformedURLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });
        MsgTrans.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/photos/send.png")));

        connectServer(port, "127.0.0.1", usernick + "（" + username + "）");
        addTrayicon();
        connectFileServer();
        loadFriendslist();
    }

    public void connectFileServer() {
        int k = 20000;
        while (true) {
            try {
                ftsv = new FileTransferServer(k);
                break;
            } catch (Exception e) {
                k++;
            }
        }
    }

    public boolean connectServer(int port, String hostIp, String name) throws BadLocationException {
        // 连接服务器
        try {
            socket = new Socket(hostIp, port);// 根据端口号和服务器ip建立连接
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 发送客户端用户基本信息(用户名和ip地址)
            sendMessage(name + "@" + socket.getLocalAddress().toString() + "@" + usersex);
            // 开启接收消息的线程
            messageThread = new MessageThread(reader, jpChat);
            messageThread.start();
            isConnected = true;// 已经连接上了
            comboBox.setSelectedIndex(0);
            connectServerBTN.setVisible(false);
            return true;
        } catch (Exception e) {
            docChat = jpChat.getStyledDocument();
            docChat.insertString(docChat.getLength(), "与端口号为：" + port + "    IP地址为：" + hostIp + "   的服务器连接失败!" + "\r\n",
                    MsgBox.getStyle("normal"));
            jpChat.setCaretPosition(docChat.getLength());
            isConnected = false;// 未连接上
            return false;
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public String getDateMessage() {
        long timemillis = System.currentTimeMillis();
        // 转换日期显示格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datemsg = usernick + "（" + username + "）" + " " + df.format(new Date(timemillis));
        String imagemsg = "#" + TipLabel.getIcon().toString() + "#";
        return imagemsg + datemsg;
    }

    public String getOutMsg(String oppo) throws BadLocationException {
        String outMSG = "";
        java.util.List<ImageIcon> list = new ArrayList<ImageIcon>();
        for (int i = 0; i < this.MsgBox.getStyledDocument().getRootElements()[0].getElement(0).getElementCount(); i++) {
            ImageIcon icon = (ImageIcon) StyleConstants.getIcon(
                    this.MsgBox.getStyledDocument().getRootElements()[0].getElement(0).getElement(i).getAttributes());
            if (icon != null) {
                list.add(icon);
            }
        }
        for (int i = 0; i < this.MsgBox.getText().length(); i++) {
            if (this.MsgBox.getStyledDocument().getCharacterElement(i).getName().equals("icon")) {
                outMSG += "#" + list.get(k).toString() + "#";
                k++;
            } else {
                outMSG += this.MsgBox.getStyledDocument().getText(i, 1);
            }
        }
        MsgBox.setText("");
        k = 0;
        return getDateMessage() + "@" + oppo + "@" + outMSG;
    }

    public JButton getPicBtn() {
        return EmojiTrans;
    }

    public void insertSendPic(ImageIcon imgIc) {
        MsgBox.insertIcon(imgIc); // 插入图片
        System.out.print(imgIc.toString());
    }

    public void loadFriendslist() {
        SelectTable sel = new SelectTable();
        int[] numbers = sel.getAllNumbers();
        for (int i = 0; numbers[i] != 0; i++) {
            sel = new SelectTable();
            sel.selectExist(numbers[i]);
            System.out.println(numbers[i]);
            User user = new User(sel.getRealName() + "（" + numbers[i] + "）", "127.0.0.1", sel.getRealSex());
            listModel_2.addElement(user);
        }
    }

    public void addTrayicon() {
        JPopupMenu Jmenu = new JPopupMenu();
        Dimension dm = new Dimension(180, 36);
        Jmenu.setUI(new BasicPopupMenuUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                g.setColor(new Color(255, 255, 255));
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        JLabel title = new JLabel("Chats Online Preview");
        title.setPreferredSize(dm);
        title.setFont(new Font("微软雅黑", Font.BOLD, 18));
        title.setForeground(Color.BLUE);
        title.setIcon(new ImageIcon(ChatForm.class.getResource("/faces/trays/product.png")));
        JMenuItem online = new JMenuItem("我在线上", new ImageIcon(ChatForm.class.getResource("/faces/trays/online.png")));
        online.setPreferredSize(dm);
        online.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        JMenuItem busy = new JMenuItem("忙碌", new ImageIcon(ChatForm.class.getResource("/faces/trays/busy.png")));
        busy.setPreferredSize(dm);
        busy.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        JMenuItem blocked = new JMenuItem("请勿打扰",
                new ImageIcon(ChatForm.class.getResource("/faces/trays/blocked.png")));
        blocked.setPreferredSize(dm);
        blocked.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        JMenuItem openmenu = new JMenuItem("打开主界面", new ImageIcon(ChatForm.class.getResource("/faces/trays/main.png")));
        openmenu.setPreferredSize(dm);
        openmenu.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        JMenuItem closemenu = new JMenuItem("退出 Chats Online",
                new ImageIcon(ChatForm.class.getResource("/faces/trays/exit.png")));
        closemenu.setPreferredSize(dm);
        closemenu.setFont(new Font("微软雅黑", Font.PLAIN, 15));

        Jmenu.add(title);
        Jmenu.addSeparator();
        Jmenu.add(online);
        Jmenu.add(busy);
        Jmenu.add(blocked);
        Jmenu.addSeparator();
        Jmenu.add(openmenu);
        Jmenu.addSeparator();
        Jmenu.add(closemenu);

        SystemTray systemtray = SystemTray.getSystemTray();

        ChatsTrayIcon trayicon = new ChatsTrayIcon(((ImageIcon) TipLabel.getIcon()).getImage(),
                "Chats Online Preview：" + usernick + "（" + username + "）" + "\n在线状态：在线\n声音：无声音\n消息提醒：后台消息弹窗", Jmenu);

        try {
            systemtray.add(trayicon);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

        trayicon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1) {
                    ChatForm.this.setVisible(true);
                }
            }
        });

        ActionListener MenuListen = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (e.getActionCommand().equals("退出 Chats Online")) {
                    systemtray.remove(trayicon);
                    if (isConnected)
                        closeConnection();
                    System.exit(0);
                } else if (e.getActionCommand().equals("打开主界面")) {
                    ChatForm.this.setVisible(true);
                }

            }
        };

        openmenu.addActionListener(MenuListen);
        closemenu.addActionListener(MenuListen);

        ChatForm.this.setVisible(true);

    }

    @SuppressWarnings("deprecation")
    public synchronized boolean closeConnection() {
        try {
            sendMessage("CLOSE");// 发送断开连接命令给服务器
            messageThread.stop();// 停止接受消息线程
            // 释放资源
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
            isConnected = false;
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            isConnected = true;
            return false;
        }
    }

    class MessageThread extends Thread {
        private BufferedReader reader;
        private JTextPane jpChat;

        // 接收消息线程的构造方法
        public MessageThread(BufferedReader reader, JTextPane jpChat) {
            this.reader = reader;
            this.jpChat = jpChat;
        }

        // 被动的关闭连接
        @SuppressWarnings("unchecked")
        public synchronized void closeCon() throws Exception {
            // 清空用户列表
            listModel = new ImageListModel();
            list_1.setModel(listModel);
            list_1.setCellRenderer(new ImageCellRender());
            list_1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            // 被动的关闭连接释放资源
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
            isConnected = false;// 修改状态为断开
        }

        @SuppressWarnings({"unchecked", "unlikely-arg-type"})
        @Override
        public void run() {
            String message = "";
            while (true) {
                try {
                    message = reader.readLine();
                    StringTokenizer stringTokenizer = new StringTokenizer(message, "/@");
                    String command = stringTokenizer.nextToken();// 命令
                    if (command.equals("CLOSE"))// 服务器已关闭命令
                    {
                        docChat = jpChat.getStyledDocument();
                        docChat.insertString(docChat.getLength(), "服务器已关闭!\r\n", MsgBox.getStyle("normal"));
                        jpChat.setCaretPosition(docChat.getLength());
                        closeCon();// 被动的关闭连接
                        comboBox.setSelectedIndex(1);
                        connectServerBTN.setVisible(true);
                        return;// 结束线程
                    } else if (command.equals("ADD")) {// 有用户上线更新在线列表
                        String username = "";
                        String userIp = "";
                        String usersex = "";
                        if ((username = stringTokenizer.nextToken()) != null
                                && (userIp = stringTokenizer.nextToken()) != null
                                && (usersex = stringTokenizer.nextToken()) != null) {
                            User user = new User(username, userIp, usersex);
                            onLineUsers.put(username, user);
                            listModel.addElement(user);
                        }
                    } else if (command.equals("DELETE")) {// 有用户下线更新在线列表
                        String username = stringTokenizer.nextToken();
                        User user = onLineUsers.get(username);
                        onLineUsers.remove(user);
                        // listModel.removeElement(user);
                    } else if (command.equals("USERLIST")) {// 加载在线用户列表
                        listModel = new ImageListModel();
                        list_1.setModel(listModel);
                        list_1.setCellRenderer(new ImageCellRender());
                        list_1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                        int size = Integer.parseInt(stringTokenizer.nextToken());
                        String username = "";
                        String userIp = "";
                        String usersex = "";
                        for (int i = 0; i < size; i++) {
                            username = stringTokenizer.nextToken();
                            userIp = stringTokenizer.nextToken();
                            usersex = stringTokenizer.nextToken();
                            User user = new User(username, userIp, usersex);
                            onLineUsers.put(username, user);
                            listModel.addElement(user);
                        }
                    } else if (command.equals("MAX")) {// 人数已达上限
                        docChat = jpChat.getStyledDocument();
                        docChat.insertString(docChat.getLength(),
                                stringTokenizer.nextToken() + stringTokenizer.nextToken() + "\r\n",
                                MsgBox.getStyle("normal"));
                        jpChat.setCaretPosition(docChat.getLength());
                        closeCon();// 被动的关闭连接
                        JOptionPane.showMessageDialog(jpChat, "服务器缓冲区已满！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;// 结束线程
                    } else {// 普通消息
                        String[] msggg = message.split("#");
                        for (int i = 0; i < msggg.length; i++) {
                            if (i % 2 == 0) {
                                docChat = jpChat.getStyledDocument();
                                docChat.insertString(docChat.getLength(), msggg[i], MsgBox.getStyle("normal"));
                                jpChat.setCaretPosition(docChat.getLength());
                            } else {
                                URL url = new URL(msggg[i]);
                                ImageIcon x = new ImageIcon(url);
                                jpChat.insertIcon(x);
                            }
                        }
                        docChat.insertString(docChat.getLength(), "\r\n", MsgBox.getStyle("normal"));
                        jpChat.setCaretPosition(docChat.getLength());
                        if (!ChatForm.this.isVisible()) {
                            tipbox.addMsg(message, usernick + "（" + username + "）");
                            tipbox.setVisible(true);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
