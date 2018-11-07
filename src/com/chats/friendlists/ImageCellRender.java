package com.chats.friendlists;

import javax.swing.*;
import java.awt.*;

public class ImageCellRender extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;
    private ImageIcon icon;

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof User) {
            User ImageUser = (User) value;
            try {
                icon = ImageUser.getImage();
                setIcon(icon);
                setText("<html><body><p style=\"font-family:等线;font-size:17;color:black\"><b>" + ImageUser.getName()
                        + "</b></p><p style=\"font-family:宋体;font-size:15;color:blue\">个性签名：<u><b>"
                        + ImageUser.getMotto() + "</b></u></body></html>");
                setVerticalTextPosition(SwingConstants.CENTER);
                setHorizontalTextPosition(SwingConstants.RIGHT);
                setOpaque(true);
                Dimension d = new Dimension(250, 40);
                setSize(d);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return this;
    }
}