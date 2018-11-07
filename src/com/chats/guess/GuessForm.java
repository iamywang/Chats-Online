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

    //��ʼ�������ʱ��ʼ�����ͻ��˶���
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

        this.setTitle("�㻭�Ҳ�");
        this.setSize(700, 500);
        this.setDefaultCloseOperation(2);
        this.setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(GuessForm.class.getResource("/Faces/icons/games.png")));
        // �ܵ����
        // JPanel panel = new JPanel();
        // panel.setBackground(Color.red);
        // �ȴ����
        waitPanel = new JPanel();
        waitPanel.setBackground(Color.WHITE);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(GuessForm.class.getResource("/faces/photos/guess.jpg")));
        waitPanel.add(label);
        this.add(waitPanel);
        // �������
        //addDrawPanel();j
//      addGuessPanel();
        this.setVisible(true);
        // �����ͻ��˿���������
        control = new ClientCtroller(socket, this);
        control.dealwith();
    }

    //��ӻ����ĺ���
    public void addDrawPanel() {
        drawPanel = new NewPanel2();
        drawPanel.setLayout(new BorderLayout());
        // ���������������
        drawLeftPanel = new JPanel();
        drawLeftPanel.setLayout(new BorderLayout());

        //��߰���м����
        centerPanel = new NewPanel2();

        //������µ���ɫ���
        colorPanel = new NewPanel2();

        //����ɫ������ÿղ���
        colorPanel.setLayout(null);

        colorPanel.setPreferredSize(new Dimension(0, 60));

        //��ɫ������ɫ��ť
        Color[] colors = {Color.black, Color.gray, Color.red, Color.orange, Color.yellow,
                Color.green, Color.blue, Color.pink, new Color(86, 4, 84)};

        //��ɫ��ť���
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

        //��ӻ��ʴ�ϸ
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
        // �����ĵ������
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
        sendBtn.setText("����");
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

    //��Ӳ����ĺ���
    public void addGuessPanel() {
        contant.setText("�µ���ʾ��Ϣ");
        sendBtn.setEnabled(false);
        drawLeftPanel.remove(colorPanel);
        drawLeftPanel.repaint();
        this.setVisible(true);
    }

    //��������
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

    //���ͼ�����
    ActionListener al = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            //��ȡ���Ϳ������
            String str = jtf.getText();
            if (str == null || str.equals("")) {
                JOptionPane.showMessageDialog(null, "�������ݲ���Ϊ�գ�");
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
