package com.chats.frames;

import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class NewMessageBox extends JWindow {

    private static final long serialVersionUID = 1L;
    private JTextPane textPane;
    private StyledDocument docChat;
    private JLabel lblNewLabel;
    private JPanel panel;
    private JToolBar toolBar;
    private JButton btnNewButton;
    private JScrollPane scrollPane;

    public NewMessageBox() {
        AWTUtilities.setWindowOpacity(this, 0.0f);
        setAlwaysOnTop(true);
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screensize.getWidth() - 450, (int) screensize.getHeight() - 250, 450, 200);
        getContentPane().setBackground(Color.WHITE);

        panel = new JPanel();
        panel.setBorder(new EmptyBorder(0, 8, 0, 0));
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0, 0));

        lblNewLabel = new JLabel("  ÏûÏ¢ºÐ×Ó");
        lblNewLabel.setIcon(new ImageIcon(NewMessageBox.class.getResource("/com/chats/faces/photos/talk.png")));
        lblNewLabel.setFont(new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 18));
        panel.add(lblNewLabel);

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(Color.WHITE);
        panel.add(toolBar, BorderLayout.EAST);

        btnNewButton = new JButton("");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewMessageBox.this.dispose();
                textPane.setText("");
            }
        });
        toolBar.add(btnNewButton);
        btnNewButton.setBackground(Color.WHITE);
        btnNewButton.setIcon(new ImageIcon(NewMessageBox.class.getResource("/com/chats/faces/photos/x.png")));

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        textPane = new JTextPane();
        scrollPane.setViewportView(textPane);
        textPane.setEditable(false);
        textPane.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
    }

    public void addMsg(String message, String name) {
        lblNewLabel.setText(" ÏûÏ¢ºÐ×Ó£º" + name);
        AWTUtilities.setWindowOpacity(this, 0.0f);
        String[] msggg = message.split("#");
        for (int i = 0; i < msggg.length; i++) {
            if (i % 2 == 0) {
                docChat = textPane.getStyledDocument();
                try {
                    docChat.insertString(docChat.getLength(), msggg[i], null);
                    textPane.setCaretPosition(docChat.getLength());
                } catch (BadLocationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                try {
                    URL url = new URL(msggg[i]);
                    ImageIcon x = new ImageIcon(url);
                    textPane.insertIcon(x);
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        try {
            docChat.insertString(docChat.getLength(), "\r\n", null);
            textPane.setCaretPosition(docChat.getLength());
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        display();
    }

    public void display() {
        class displayTask extends TimerTask {
            @Override
            public void run() {
                try {
                    if (NewMessageBox.this.getOpacity() != 1.0f)
                        AWTUtilities.setWindowOpacity(NewMessageBox.this, NewMessageBox.this.getOpacity() + 0.05f);
                } catch (Exception e) {

                }
            }
        }
        Timer timer = new Timer();
        timer.schedule(new displayTask(), 0, 100);
    }
}
