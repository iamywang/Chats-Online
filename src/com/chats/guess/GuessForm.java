package com.chats.guess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;

public class GuessForm extends JFrame {

    public JButton sendBtn;
    public JLabel contant;

    public NewPanel2 colorPanel, drawPanel, centerPanel;
    public JPanel waitPanel;
    public JPanel drawLeftPanel;
    public JTextField jtf;
    public JTextArea jta;
    public Graphics2D g;
    public Color color;
    public ClientCtroller control;
    public Socket socket;
    public int x1, y1;
    public BasicStroke strock;
    public JComboBox<Integer> box;

    //初始化界面的时候开始创建客户端对象
    class NewPanel2 extends JPanel {
        public NewPanel2() {

        }

        public void paintComponent(Graphics g) {
            int x = 0, y = 0;
            ImageIcon icon = new ImageIcon(GuessForm.class.getResource("/faces/photos/drawpanel.jpg"));
            g.drawImage(icon.getImage(), x, y, getSize().width, getSize().height, this);

        }
    }

    public GuessForm() {

        try {
            socket = new Socket("localhost", 12345);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initFrame();
    }


    public void initFrame() {

        this.setTitle("你画我猜");
        this.setSize(700, 500);
        this.setDefaultCloseOperation(2);
        this.setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(GuessForm.class.getResource("/Faces/icons/games.png")));
        // 总的面板
        // JPanel panel = new JPanel();
        // panel.setBackground(Color.red);
        // 等待面板
        waitPanel = new JPanel();
        waitPanel.setBackground(Color.WHITE);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(GuessForm.class.getResource("/faces/photos/guess.jpg")));
        waitPanel.add(label);
        this.add(waitPanel);
        // 画的面板
        //addDrawPanel();j
//      addGuessPanel();
        this.setVisible(true);
        // 创建客户端控制器对象
        control = new ClientCtroller(socket, this);
        control.dealwith();
    }

    //添加画面板的函数
    public void addDrawPanel() {
        drawPanel = new NewPanel2();
        drawPanel.setLayout(new BorderLayout());
        // 画面板的左右子面板
        drawLeftPanel = new JPanel();
        drawLeftPanel.setLayout(new BorderLayout());

        //左边板的中间面板
        centerPanel = new NewPanel2();

        //左面板下的颜色面板
        colorPanel = new NewPanel2();

        //给颜色面板设置空布局
        colorPanel.setLayout(null);

        colorPanel.setPreferredSize(new Dimension(0, 60));

        //颜色面板的颜色按钮
        Color[] colors = {Color.black, Color.gray, Color.red, Color.orange, Color.yellow,
                Color.green, Color.blue, Color.pink, new Color(86, 4, 84)};

        //颜色按钮添加
        ActionListener btnlistener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                JButton bt = (JButton) e.getSource();
                color = bt.getBackground();
            }
        };
        for (int i = 0; i < colors.length; i++) {
            JButton btn = new JButton();
            btn.setBackground(colors[i]);
            btn.addActionListener(btnlistener);
            btn.setBounds(40 + i * 20, 15, 20, 30);
            colorPanel.add(btn);
        }
        ImageIcon i1 = new ImageIcon(GuessForm.class.getResource("/faces/photos/xiangpi.jpg"));
        JButton bt99 = new JButton(i1);

        bt99.setBounds(270, 15, i1.getIconWidth(), i1.getIconHeight() - 7);
        bt99.setBorderPainted(false);
        bt99.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                color = Color.WHITE;
            }
        });
        colorPanel.add(bt99);

        //添加画笔粗细
        box = new JComboBox<Integer>();
        box.setBounds(380, 15, 80, 30);
        for (int i = 0; i < 10; i++) {
            Integer intdata = new Integer(i + 1);
            box.addItem(intdata);
        }
        colorPanel.add(box);

        JPanel drawRightPanel = new JPanel();
        drawRightPanel.setBackground(new Color(231, 204, 129));
        drawRightPanel.setLayout(new BorderLayout());
        drawRightPanel.setPreferredSize(new Dimension(200, 0));
        // 右面板的的下面板
        NewPanel2 buttonPanel = new NewPanel2();
        buttonPanel.setPreferredSize(new Dimension(0, 60));
        jta = new JTextArea();
        jta.setLineWrap(false);
        jta.setOpaque(false);
        jta.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);

        jtf = new JTextField(11);
        contant = new JLabel();
        sendBtn = new JButton();
        sendBtn.setText("发送");
        sendBtn.addActionListener(al);
        buttonPanel.add(jtf);
        buttonPanel.add(sendBtn);

        drawRightPanel.add(jsp);
        drawRightPanel.add(buttonPanel, BorderLayout.SOUTH);

        contant.setPreferredSize(new Dimension(0, 20));
        drawLeftPanel.add(contant, BorderLayout.NORTH);
        drawLeftPanel.add(centerPanel, BorderLayout.CENTER);
        drawLeftPanel.add(colorPanel, BorderLayout.SOUTH);
        drawPanel.add(drawLeftPanel);
        drawPanel.add(drawRightPanel, BorderLayout.EAST);
        this.add(drawPanel);
        centerPanel.addMouseListener(ma);
        centerPanel.addMouseMotionListener(ma);
        this.setVisible(true);
        g = (Graphics2D) centerPanel.getGraphics();
    }

    //添加猜面板的函数
    public void addGuessPanel() {
        contant.setText("猜的提示信息");
        sendBtn.setEnabled(false);
        drawLeftPanel.remove(colorPanel);
        drawLeftPanel.repaint();
        this.setVisible(true);
    }

    //鼠标监听器
    MouseAdapter ma = new MouseAdapter() {

        public void mousePressed(MouseEvent e) {
            x1 = e.getX();
            y1 = e.getY();
        }

        public void mouseEntered(MouseEvent e) {
            if (color == null) {
                color = Color.black;
            }

//          System.out.println(i);
            g.setColor(color);

        }

        public void mouseDragged(MouseEvent e) {
            int width = (int) box.getSelectedItem();
            strock = new BasicStroke(width);
            g.setStroke(strock);

            int x2 = e.getX();
            int y2 = e.getY();
            g.drawLine(x1, y1, x2, y2);
//          x1 = x2;
//          y1 = y2;
            try {

                control.sendMsg1(socket.getOutputStream(), x1, y1, x2, y2, g.getColor().getRGB(), width);
                x1 = x2;
                y1 = y2;
            } catch (IOException e1) {
            }
        }

    };

    //发送监听器
    ActionListener al = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            //获取发送框的内容
            String str = jtf.getText();
            if (str == null || str.equals("")) {
                JOptionPane.showMessageDialog(null, "发送内容不能为空！");
            } else {
                try {
                    control.dos.writeUTF(str);
                    jtf.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    };

}
