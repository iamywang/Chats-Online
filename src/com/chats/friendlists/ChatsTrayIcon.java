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

        // 初始化JDialog
        dialog = new JDialog();
        dialog.setUndecorated(true);// 取消窗体装饰
        dialog.setAlwaysOnTop(true);// 设置窗体始终位于上方

        // 设置系统图标大小为自动调整
        this.setImageAutoSize(true);

        // 为TrayIcon设置鼠标监听器
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {

                // 鼠标右键在组件上释放时调用，显示弹出菜单
                if (e.getButton() == MouseEvent.BUTTON3 && Jmenu != null) {

                    // 设置dialog的显示位置
                    Dimension size = Jmenu.getPreferredSize();
                    dialog.setLocation(e.getX() - size.width - 3, e.getY() - size.height - 3);
                    dialog.setVisible(true);

                    // 显示弹出菜单Jmenu
                    Jmenu.show(dialog.getContentPane(), 0, 0);
                }
            }
        });

        // 为弹出菜单添加监听器
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