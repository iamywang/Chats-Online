package com.chats.faces;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShowWindow extends JFrame implements MouseListener {
    private static final long serialVersionUID = 1L;
    static Point origin = new Point();
    private ImageIcon ii;
    private JButton save;
    private JButton cancel;
    private int isExit;
    private String filename;

    private JFileChooser chooser;

    public ShowWindow(ImageIcon ii, int isExit, String filename) {
        this.filename = filename;
        this.isExit = isExit;
        this.ii = ii;
        initUI();
        initLayout();
        setSize(ii.getIconWidth(), ii.getIconHeight() + 30);
        setUndecorated(true);
        setLocationRelativeTo(null);
        WindowMove();
        setVisible(true);
    }

    private void initUI() {
        save = new JButton("����");
        cancel = new JButton("ȡ��");
        save.addMouseListener(this);
        cancel.addMouseListener(this);
        chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);
    }

    private void initLayout() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        JPanel btnpane = new JPanel(new GridLayout(1, 8));
        btnpane.add(save);
        btnpane.add(cancel);
        main.add(new JLabel(ii), BorderLayout.CENTER);
        main.add(btnpane, BorderLayout.SOUTH);
        add(main);
    }

    // �����ƶ�����
    public void WindowMove() {
        // ����û�б���Ĵ��ڿ����϶�
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                origin.x = e.getX(); // ����갴�µ�ʱ���ô��ڵ�ǰ��λ��
                origin.y = e.getY();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == cancel) {
            if (isExit == 0) {
                dispose();
            } else {
                ShowWindow.this.dispose();
            }
        }
        if (e.getSource() == save) {
            File file = null;
            chooser.setSelectedFile(new File(filename));
            int returnVal = chooser.showSaveDialog(ShowWindow.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
            }
            Image img = ii.getImage();

            try {
                if (file != null) {
                    ImageIO.write((BufferedImage) img, "png", file);
                    JOptionPane.showMessageDialog(ShowWindow.this, "����ɹ�", "��ܰ��ʾ", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
