package com.chats.faces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShot extends JFrame {
    private static final long serialVersionUID = 1L;
    private Image image;
    private JLabel imageLabel;
    private int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    private int isExit;
    private String filename;
    private int x, y, xEnd, yEnd; // 用于记录鼠标点击开始和结束的坐标

    public ScreenShot(int isExit, String filename) throws AWTException, InterruptedException {
        this.isExit = isExit;
        this.filename = filename;

        image = GraphicsUtils.getScreenImage(0, 0, width, height);

        imageLabel = new JLabel(new ImageIcon(image));

        imageLabel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        createAction();
        getContentPane().add(imageLabel);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createAction() {
        imageLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown()) {
                    if (isExit == 0) {
                        System.exit(0);
                    } else {
                        ScreenShot.this.dispose();
                    }
                }
            }

            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                xEnd = e.getX();
                yEnd = e.getY();
                try {
                    image = GraphicsUtils.getScreenImage(Math.min(x, xEnd), Math.min(y, yEnd), Math.abs(xEnd - x),
                            Math.abs(yEnd - y));
                } catch (AWTException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                // 为了查看截图效果，将区域截图的部分代替全屏的截图展示
                imageLabel.setIcon(new ImageIcon(image));

                if (image != null) {
                    new ShowWindow(new ImageIcon(image), isExit, filename);
                    ScreenShot.this.dispose();
                }
            }
        });
        imageLabel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {

                xEnd = e.getX();
                yEnd = e.getY();
                BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null),
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = (Graphics2D) bi.getGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.setColor(Color.RED);
                g2d.drawRect(Math.min(x, xEnd) - 1, Math.min(y, yEnd) - 1, Math.abs(xEnd - x) + 1,
                        Math.abs(yEnd - y) + 1);
                g2d.dispose();

                Graphics g = imageLabel.getGraphics();
                g.drawImage(bi, 0, 0, null);
                g.dispose();
            }

            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();

                BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null),
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = (Graphics2D) bi.getGraphics();
                g2d.drawImage(image, 0, 0, null);
                g2d.setColor(Color.RED);
                g2d.drawLine(x, 0, x, height);
                g2d.drawLine(0, y, width, y);
                g2d.dispose();
                Graphics g = imageLabel.getGraphics();
                g.drawImage(bi, 0, 0, null);
                g.dispose();
            }
        });
    }

    public static void main(String[] args) throws AWTException, InterruptedException {
        String filename = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date()) + ".png";
        new ScreenShot(0, filename);
    }
}

class GraphicsUtils {

    public static BufferedImage getScreenImage(int x, int y, int w, int h) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        BufferedImage screen = null;
        if (w - x > 0) {
            screen = robot.createScreenCapture(new Rectangle(x, y, w, h));
        }
        return screen;
    }
}