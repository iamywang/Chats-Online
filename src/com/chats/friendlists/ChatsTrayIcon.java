package com.chats.friendlists;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatsTrayIcon extends TrayIcon {

    private JDialog dialog;

    public ChatsTrayIcon(Image image, String ps, JPopupMenu Jmenu) {
        super(image, ps);

        // ��ʼ��JDialog
        dialog = new JDialog();
        dialog.setUndecorated(true);// ȡ������װ��
        dialog.setAlwaysOnTop(true);// ���ô���ʼ��λ���Ϸ�

        // ����ϵͳͼ���СΪ�Զ�����
        this.setImageAutoSize(true);

        // ΪTrayIcon������������
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                // ����Ҽ���������ͷ�ʱ���ã���ʾ�����˵�
                if (e.getButton() == MouseEvent.BUTTON3 && Jmenu != null) {

                    // ����dialog����ʾλ��
                    Dimension size = Jmenu.getPreferredSize();
                    dialog.setLocation(e.getX() - size.width - 3, e.getY() - size.height - 3);
                    dialog.setVisible(true);

                    // ��ʾ�����˵�Jmenu
                    Jmenu.show(dialog.getContentPane(), 0, 0);
                }
            }
        });

        // Ϊ�����˵���Ӽ�����
        Jmenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                dialog.setVisible(false);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                dialog.setVisible(false);
            }
        });

    }

}