package com.chats.faces;

import com.chats.frames.ChatForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FacesWindow extends JWindow {
    private static final long serialVersionUID = 1L;
    GridLayout gridLayout1 = new GridLayout(7, 15);
    JLabel[] ico = new JLabel[105]; /* 放表情 */
    int i;
    ChatForm owner;
    String[] intro = {"",};/* 图片描述 */

    public FacesWindow(ChatForm owner) {
        super(owner);
        this.owner = owner;
        try {
            init();
            this.setAlwaysOnTop(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void init() {
        this.setPreferredSize(new Dimension(36 * 15, 36 * 7));
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        this.setContentPane(panel);
        panel.setLayout(new BorderLayout(0, 0));
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(gridLayout1);
        panel.add(p);

        JLabel lblNewLabel = new JLabel("表情列表");
        lblNewLabel.setBackground(Color.WHITE);
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblNewLabel, BorderLayout.NORTH);
        String fileName = "";
        for (i = 0; i < ico.length; i++) {
            fileName = "/com/chats/faces/images/Expression_(" + (i + 1) + ").png";/* 修改图片路径 */
            ico[i] = new JLabel(new ChatPic(FacesWindow.class.getResource(fileName), i), SwingConstants.CENTER);
            ico[i].setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
            ico[i].setToolTipText("聊天表情" + (i + 1));
            ico[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == 1) {
                        JLabel cubl = (JLabel) (e.getSource());
                        ChatPic cupic = (ChatPic) (cubl.getIcon());
                        owner.insertSendPic(cupic);
                        cubl.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
                        getObj().dispose();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    ((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLUE));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    ((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
                }

            });
            p.add(ico[i]);
        }
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                getObj().dispose();
            }

        });
    }

    @Override
    public void setVisible(boolean show) {
        if (show) {
            determineAndSetLocation();
        }
        super.setVisible(show);
    }

    private void determineAndSetLocation() {
        Point loc = owner.getPicBtn().getLocationOnScreen();/* 控件相对于屏幕的位置 */
        setBounds(loc.x - getPreferredSize().width / 3, loc.y - getPreferredSize().height, getPreferredSize().width,
                getPreferredSize().height);
    }

    private JWindow getObj() {
        return this;
    }

}
