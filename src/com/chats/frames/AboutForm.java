package com.chats.frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel TitleLabel;
    private JLabel info;
    private JButton button;
    private JLabel label;

    public AboutForm() {
        setType(Type.UTILITY);
        setResizable(false);
        setAutoRequestFocus(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(Color.WHITE);
        setTitle("���ڱ���Ʒ");
        setBounds(0, 0, 600, 350);
        this.setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        TitleLabel = new JLabel("  ���� Chats Online Preview");
        TitleLabel.setIcon(new ImageIcon(AboutForm.class.getResource("/faces/photos/about.png")));
        TitleLabel.setForeground(new Color(0, 0, 128));
        TitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TitleLabel.setFont(new Font("΢���ź�", Font.BOLD, 24));
        TitleLabel.setBounds(0, 8, 590, 64);
        contentPane.add(TitleLabel);

        JLabel lblCopyrightxx = new JLabel(
                "Copyright\u00A9XX 2018 \u7248\u6743\u6240\u6709\uFF0C\u4FB5\u6743\u5FC5\u7A76\u3002");
        lblCopyrightxx.setHorizontalAlignment(SwingConstants.RIGHT);
        lblCopyrightxx.setFont(new Font("΢���ź�", Font.PLAIN, 15));
        lblCopyrightxx.setBounds(280, 278, 310, 32);
        contentPane.add(lblCopyrightxx);

        info = new JLabel();
        info.setHorizontalAlignment(SwingConstants.LEFT);
        info.setText("<html><font style = color:gray>��Ʒ���ƣ�<font style = color:black>Chats Online"
                + "<br><font style = color:gray>��Ʒ�汾��<font style = color:black>1.0 Preview"
                + "<br><font style = color:gray>��װʱ�䣺<font style = color:black>2018��5��"
                + "<br><font style = color:gray>����������<font style = color:black>Eclipse Photon Platform"
                + "<br><font style = color:gray>���л�����<font style = color:black>Java Virtual Machine"
                + "<br><font style = color:gray>��Ȩ��Ϣ��<font style = color:black>�����Ȩ"
                + "<br><font style = color:gray>�����ߣ�<font style = color:black>2 people");
        info.setFont(new Font("΢���ź�", Font.PLAIN, 16));
        info.setBounds(200, 76, 390, 200);
        contentPane.add(info);

        button = new JButton("\u68C0\u67E5\u66F4\u65B0");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("�������°汾��");
            }
        });
        button.setToolTipText("");
        button.setForeground(new Color(0, 255, 0));
        button.setFont(new Font("΢���ź�", Font.PLAIN, 15));
        button.setBackground(new Color(0, 0, 128));
        button.setBounds(4, 278, 120, 32);
        contentPane.add(button);

        label = new JLabel("");
        label.setForeground(Color.BLUE);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setFont(new Font("΢���ź�", Font.PLAIN, 15));
        label.setBounds(128, 278, 148, 32);
        contentPane.add(label);

        JLabel image = new JLabel("");
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setIcon(new ImageIcon(AboutForm.class.getResource("/com/chats/faces/icons/chatform.png")));
        image.setBounds(0, 76, 200, 200);
        contentPane.add(image);
    }
}
