package com.chats.frames;

import com.chats.database.InsertData;
import com.chats.database.SelectTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StartForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel startContentPane, spanel1, spanel2, spanel3;
    private JPasswordField inUserPassword;
    private JFormattedTextField inUserNumber;
    private JButton startRegister, startLogin;
    private JLabel startTitle, startTiplabel, startTip1, startTip2, startLoadingImage, startNickname, startForget,
            startCopyright;
    private JCheckBox inUserRemember, inUserAutoLogin;

    private JPanel registerContentPane;
    private JTextField NameField, teleField, checkField, ageField, mottoField;
    private JPasswordField PasswordField;
    private InsertData indata = new InsertData();
    private SelectTable seldata = new SelectTable();
    private JLabel AutoNumberLabel, registerTip, registerImage, registerCopyright, tofLabel, rlabel1, rlabel2, rlabel3,
            rlabel4, rlabel5, rlabel7, rlabel8, rlabel9;
    private JComboBox<String> SexBox;
    private Random rand;
    private JButton rRegister, registerChoosefile;
    private JButton button;

    public StartForm() {
        setResizable(false);
        setAutoRequestFocus(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(StartForm.class.getResource("/faces/icons/login.png")));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 12));
        setBackground(Color.WHITE);
        setTitle("µ«¬ºªÚ◊¢≤·");
        setBounds(0, 0, 700, 370);
        this.setLocationRelativeTo(null);
        start();
//		register();
    }

    public void start() {
        startContentPane = new JPanel();
        startContentPane.setForeground(Color.WHITE);
        startContentPane.setBackground(Color.WHITE);
        startContentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(startContentPane);
        startContentPane.setLayout(null);

        startLoadingImage = new JLabel("");
        startLoadingImage.setBackground(Color.WHITE);
        startLoadingImage.setHorizontalAlignment(SwingConstants.CENTER);
        startLoadingImage.setBounds(0, 74, 700, 261);
        startContentPane.add(startLoadingImage);

        JLabel startImage = new JLabel("");
        startImage.setIcon(new ImageIcon(StartForm.class.getResource("/faces/photos/userlist.png")));
        startImage.setBounds(300, 85, 100, 100);
        startContentPane.add(startImage);
        startImage.setHorizontalAlignment(SwingConstants.CENTER);
        startImage.setBackground(Color.WHITE);

        spanel1 = new JPanel();
        spanel1.setBounds(404, 95, 250, 32);
        startContentPane.add(spanel1);
        spanel1.setBackground(Color.WHITE);
        spanel1.setLayout(new BorderLayout(0, 0));

        startTip1 = new JLabel("\u8D26\u53F7  ");
        startTip1.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));
        spanel1.add(startTip1, BorderLayout.WEST);

        inUserNumber = new JFormattedTextField();
        spanel1.add(inUserNumber, BorderLayout.CENTER);
        inUserNumber.addInputMethodListener(new InputMethodListener() {
            @Override
            public void caretPositionChanged(InputMethodEvent arg0) {
            }

            @Override
            public void inputMethodTextChanged(InputMethodEvent arg0) {
                if (!inUserNumber.getText().equals("")
                        && seldata.selectExist(Integer.parseInt(inUserNumber.getText())) == 1) {
                    startImage.setIcon(seldata.getImage());
                    startNickname.setText(seldata.getRealName());
                } else {
                }
            }
        });
        inUserNumber.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));

        spanel2 = new JPanel();
        spanel2.setBounds(404, 143, 250, 32);
        startContentPane.add(spanel2);
        spanel2.setBackground(Color.WHITE);
        spanel2.setLayout(new BorderLayout(0, 0));

        startTip2 = new JLabel("\u5BC6\u7801  ");
        startTip2.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));
        spanel2.add(startTip2, BorderLayout.WEST);

        inUserPassword = new JPasswordField();
        spanel2.add(inUserPassword, BorderLayout.CENTER);
        inUserPassword.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));

        spanel3 = new JPanel();
        spanel3.setBounds(404, 188, 280, 29);
        startContentPane.add(spanel3);
        spanel3.setBackground(Color.WHITE);
        spanel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        inUserAutoLogin = new JCheckBox("\u81EA\u52A8\u767B\u5F55");
        spanel3.add(inUserAutoLogin);
        inUserAutoLogin.setHorizontalAlignment(SwingConstants.CENTER);
        inUserAutoLogin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                if (inUserAutoLogin.isSelected())
                    inUserRemember.setSelected(true);
            }
        });
        inUserAutoLogin.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        inUserAutoLogin.setBackground(Color.WHITE);
        inUserAutoLogin.setForeground(Color.BLACK);

        inUserRemember = new JCheckBox("\u8BB0\u4F4F\u5BC6\u7801");
        spanel3.add(inUserRemember);
        inUserRemember.setHorizontalAlignment(SwingConstants.CENTER);
        inUserRemember.setForeground(Color.BLACK);
        inUserRemember.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        inUserRemember.setBackground(Color.WHITE);

        startForget = new JLabel("\u5FD8\u8BB0\u5BC6\u7801");
        startForget.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent arg0) {
                startForget.setForeground(Color.GRAY);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                startForget.setForeground(Color.BLACK);
            }
        });
        startForget.setForeground(Color.GRAY);
        startForget.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        spanel3.add(startForget);

        inUserRemember.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (inUserRemember.isSelected() == false)
                    inUserAutoLogin.setSelected(false);
            }
        });

        startRegister = new JButton("\u6CE8\u518C");
        startRegister.setForeground(Color.WHITE);
        startRegister.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        startRegister.setBackground(new Color(0, 100, 0));
        startRegister.setBounds(300, 230, 170, 36);
        startContentPane.add(startRegister);

        startLogin = new JButton("\u767B\u5F55");
        startLogin.setForeground(Color.WHITE);
        startLogin.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        startLogin.setBackground(new Color(47, 79, 79));
        startLogin.setBounds(484, 230, 170, 36);
        startContentPane.add(startLogin);

        startTiplabel = new JLabel();
        startTiplabel.setBounds(300, 290, 354, 28);
        startContentPane.add(startTiplabel);
        startTiplabel.setForeground(Color.BLACK);
        startTiplabel.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        startTiplabel.setHorizontalAlignment(SwingConstants.CENTER);

        startTitle = new JLabel("Chats Online Preview");
        startTitle.setForeground(new Color(0, 0, 128));
        startTitle.setIcon(new ImageIcon(StartForm.class.getResource("/faces/photos/chats.png")));
        startTitle.setHorizontalAlignment(SwingConstants.CENTER);
        startTitle.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 24));
        startTitle.setBounds(0, 10, 700, 64);
        startContentPane.add(startTitle);

        startCopyright = new JLabel("\u00A9XX 2018 \u7248\u6743\u6240\u6709\uFF0C\u4FB5\u6743\u5FC5\u7A76\u3002");
        startCopyright.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        startCopyright.setHorizontalAlignment(SwingConstants.CENTER);
        startCopyright.setBounds(0, 290, 300, 28);
        startContentPane.add(startCopyright);

        startNickname = new JLabel("");
        startNickname.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        startNickname.setHorizontalAlignment(SwingConstants.CENTER);
        startNickname.setBackground(Color.WHITE);
        startNickname.setBounds(300, 188, 100, 29);
        startContentPane.add(startNickname);
        startLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inUserNumber.getText().equals(null) || inUserNumber.getText().equals("")
                        || inUserPassword.getPassword().toString().equals(null)
                        || inUserPassword.getPassword().toString().equals("")) {
                    startTiplabel.setText("«Î ‰»Î’À∫≈√‹¬Î∫Ûµ«¬º£°");
                    startTiplabel.setForeground(Color.RED);
                    return;
                }
                class LoadingProgressBarThread extends Thread {
                    public void run() {
                        startLoadingImage
                                .setIcon(new ImageIcon(StartForm.class.getResource("/faces/photos/loading.gif")));
                        startLoadingImage.setOpaque(true);
                        login();
                    }
                }
                LoadingProgressBarThread loading = new LoadingProgressBarThread();
                loading.start();
            }
        });
        startRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
                StartForm.this.setContentPane(registerContentPane);
            }
        });
    }

    public void register() {
        registerContentPane = new JPanel();
        registerContentPane.setBackground(Color.WHITE);
        registerContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(registerContentPane);

        rand = new Random();
        long num = rand.nextInt(9000000) + 1000000;
        while (seldata.selectExist(num) != -1) {
            num = rand.nextInt(9000000) + 1000000;
        }
        registerContentPane.setLayout(null);

        rlabel2 = new JLabel("\u53F7\u7801");
        rlabel2.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel2.setBounds(300, 76, 64, 32);
        registerContentPane.add(rlabel2);
        rlabel2.setToolTipText("");
        rlabel2.setHorizontalAlignment(SwingConstants.CENTER);

        rlabel4 = new JLabel("\u5BC6\u7801");
        rlabel4.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel4.setBounds(300, 148, 64, 32);
        registerContentPane.add(rlabel4);
        rlabel4.setToolTipText("");
        rlabel4.setHorizontalAlignment(SwingConstants.CENTER);

        rlabel3 = new JLabel("\u6635\u79F0");
        rlabel3.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel3.setBounds(300, 112, 64, 32);
        registerContentPane.add(rlabel3);
        rlabel3.setToolTipText("");
        rlabel3.setHorizontalAlignment(SwingConstants.CENTER);

        rlabel7 = new JLabel("\u6027\u522B");
        rlabel7.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel7.setBounds(300, 220, 64, 32);
        registerContentPane.add(rlabel7);
        rlabel7.setToolTipText("");
        rlabel7.setHorizontalAlignment(SwingConstants.CENTER);

        rlabel9 = new JLabel("\u4E2A\u6027\u7B7E\u540D");
        rlabel9.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel9.setBounds(300, 256, 64, 32);
        registerContentPane.add(rlabel9);
        rlabel9.setToolTipText("");
        rlabel9.setHorizontalAlignment(SwingConstants.CENTER);

        registerTip = new JLabel();
        registerTip.setBounds(8, 296, 498, 30);
        registerContentPane.add(registerTip);
        registerTip.setForeground(Color.RED);
        registerTip.setHorizontalAlignment(SwingConstants.CENTER);
        registerTip.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));

        rRegister = new JButton("\u6CE8\u518C");
        rRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (NameField.getText().equals(null) || NameField.getText().equals("")
                        || PasswordField.getPassword().toString().equals(null)
                        || PasswordField.getPassword().toString().equals("") || mottoField.getText().equals(null)
                        || mottoField.getText().equals("") || ageField.getText().equals(null)
                        || ageField.getText().equals(""))
                    registerTip.setText("«Î ‰»ÎÕÍ’˚–≈œ¢∫Û‘ŸΩ¯––◊¢≤·£°");
                else {
                    String pswd = new String(PasswordField.getPassword());
                    indata.insertNew(Integer.parseInt(AutoNumberLabel.getText()), NameField.getText(), pswd,
                            (String) SexBox.getSelectedItem(), mottoField.getText(),
                            registerImage.getIcon().toString());
                    StartForm.this.setContentPane(startContentPane);
                }
            }
        });
        rRegister.setForeground(new Color(255, 255, 0));
        rRegister.setBackground(new Color(25, 25, 112));
        rRegister.setBounds(520, 296, 170, 30);
        registerContentPane.add(rRegister);

        rRegister.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));

        AutoNumberLabel = new JLabel(num + "");
        AutoNumberLabel.setBounds(370, 76, 120, 32);
        registerContentPane.add(AutoNumberLabel);
        AutoNumberLabel.setForeground(Color.RED);
        AutoNumberLabel.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 24));

        PasswordField = new JPasswordField();
        PasswordField.setBounds(370, 148, 272, 32);
        registerContentPane.add(PasswordField);
        PasswordField.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));

        NameField = new JTextField();
        NameField.setBounds(370, 112, 272, 32);
        registerContentPane.add(NameField);
        NameField.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));
        NameField.setColumns(10);

        SexBox = new JComboBox<String>();
        SexBox.setBounds(370, 220, 100, 32);
        registerContentPane.add(SexBox);
        SexBox.setBackground(Color.WHITE);
        SexBox.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));
        SexBox.setMaximumRowCount(2);
        SexBox.setModel(new DefaultComboBoxModel<String>(new String[]{"ƒ–", "≈Æ"}));

        registerImage = new JLabel("");
        registerImage.setIcon(new ImageIcon(StartForm.class.getResource("/com/chats/faces/photos/userlist.png")));
        registerImage.setBounds(8, 112, 140, 140);
        registerContentPane.add(registerImage);
        registerImage.setHorizontalAlignment(SwingConstants.CENTER);
        registerImage.setBackground(Color.WHITE);

        rlabel1 = new JLabel("\u5934\u50CF");
        rlabel1.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel1.setBounds(8, 76, 284, 32);
        registerContentPane.add(rlabel1);
        rlabel1.setHorizontalAlignment(SwingConstants.CENTER);

        mottoField = new JTextField();
        mottoField.setBounds(370, 256, 272, 32);
        registerContentPane.add(mottoField);
        mottoField.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        mottoField.setColumns(10);

        registerChoosefile = new JButton("\u9009\u62E9\u6587\u4EF6");
        registerChoosefile.setBackground(new Color(128, 0, 0));
        registerChoosefile.setForeground(Color.CYAN);
        registerChoosefile.setBounds(152, 220, 140, 32);
        registerContentPane.add(registerChoosefile);
        registerChoosefile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Õº∆¨(÷ß≥÷ bmp,jpg,png,gif ∏Ò Ω)", "bmp", "jpg",
                        "png", "gif");
                jfc.setFileFilter(filter);
                int rtn = jfc.showOpenDialog(StartForm.this);
                if (rtn == JFileChooser.APPROVE_OPTION) {
                    try {
                        String filePath = "file:/" + jfc.getSelectedFile().getAbsolutePath();
                        URL url = new URL(filePath);
                        ImageIcon icon = new ImageIcon(url);
                        registerImage.setIcon(icon);
                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });
        registerChoosefile.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));

        button = new JButton("<");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                start();
                StartForm.this.setContentPane(startContentPane);
            }
        });
        button.setForeground(Color.WHITE);
        button.setFont(new Font("°∂", Font.BOLD, 26));
        button.setBackground(Color.BLACK);
        button.setBounds(10, 18, 48, 48);
        registerContentPane.add(button);

        JLabel registerTitle = new JLabel("Chats Online Preview");
        registerTitle.setIcon(new ImageIcon(StartForm.class.getResource("/com/chats/faces/photos/chats.png")));
        registerTitle.setHorizontalAlignment(SwingConstants.CENTER);
        registerTitle.setForeground(new Color(0, 0, 128));
        registerTitle.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 24));
        registerTitle.setBounds(0, 10, 690, 64);
        registerContentPane.add(registerTitle);

        rlabel8 = new JLabel("\u5E74\u9F84");
        rlabel8.setToolTipText("");
        rlabel8.setHorizontalAlignment(SwingConstants.CENTER);
        rlabel8.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel8.setBounds(474, 220, 64, 32);
        registerContentPane.add(rlabel8);

        ageField = new JTextField();
        ageField.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));
        ageField.setColumns(10);
        ageField.setBounds(542, 220, 100, 32);
        registerContentPane.add(ageField);

        rlabel5 = new JLabel("\u624B\u673A\u53F7");
        rlabel5.setToolTipText("");
        rlabel5.setHorizontalAlignment(SwingConstants.CENTER);
        rlabel5.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel5.setBounds(300, 184, 64, 32);
        registerContentPane.add(rlabel5);

        tofLabel = new JLabel("");
        tofLabel.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 20));
        tofLabel.setBounds(646, 184, 32, 32);
        registerContentPane.add(tofLabel);

        registerCopyright = new JLabel("\u00A9XX 2018 \u7248\u6743\u6240\u6709\uFF0C\u4FB5\u6743\u5FC5\u7A76\u3002");
        registerCopyright.setHorizontalAlignment(SwingConstants.CENTER);
        registerCopyright.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        registerCopyright.setBounds(8, 256, 284, 32);
        registerContentPane.add(registerCopyright);

        teleField = new JTextField();
        teleField.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));
        teleField.setColumns(11);
        teleField.setBounds(370, 184, 120, 32);
        registerContentPane.add(teleField);

        JLabel rlabel6 = new JLabel("\u9A8C\u8BC1\u7801");
        rlabel6.setToolTipText("");
        rlabel6.setHorizontalAlignment(SwingConstants.CENTER);
        rlabel6.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        rlabel6.setBounds(494, 184, 64, 32);
        registerContentPane.add(rlabel6);

        checkField = new JTextField();
        checkField.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 16));
        checkField.setColumns(10);
        checkField.setBounds(562, 184, 80, 32);
        registerContentPane.add(checkField);

        JButton registerGetcheck = new JButton("ªÒ»°—È÷§¬Î");
        registerGetcheck.addActionListener(new ActionListener() {
            int k;

            @Override
            public void actionPerformed(ActionEvent e) {
                k = 30;
                class Registertime extends TimerTask {
                    @Override
                    public void run() {
                        if (k > 0) {
                            registerGetcheck.setText("ªÒ»°—È÷§¬Î (" + k + "s)");
                            registerGetcheck.setEnabled(false);
                            k--;
                        } else {
                            registerGetcheck.setEnabled(true);
                            registerGetcheck.setText("ªÒ»°—È÷§¬Î");
                        }
                    }
                }
                Timer timer = new Timer();
                timer.schedule(new Registertime(), 0, 1000);
            }
        });
        registerGetcheck.setForeground(new Color(230, 230, 250));
        registerGetcheck.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 15));
        registerGetcheck.setBackground(new Color(128, 0, 128));
        registerGetcheck.setBounds(492, 76, 150, 32);
        registerContentPane.add(registerGetcheck);
        class checkRegisterBTN extends TimerTask {
            @Override
            public void run() {
                if (checkField.getText().equals(AutoNumberLabel.getText())) {
                    tofLabel.setText("°Ã");
                    tofLabel.setForeground(Color.GREEN);
                    rRegister.setEnabled(true);
                } else {
                    rRegister.setEnabled(false);
                    tofLabel.setText("°¡");
                    tofLabel.setForeground(Color.RED);
                }
            }

        }
        Timer timer = new Timer();
        timer.schedule(new checkRegisterBTN(), 1, 1);
    }

    public void login() {
        if (seldata.selectExist(Integer.parseInt(inUserNumber.getText())) == 1) {
            String pswd = new String(inUserPassword.getPassword());
            if (seldata.getRealpwd().equals(pswd)) {
                System.out.println("login successfully!");
                try {
                    ChatForm Cform = new ChatForm(seldata.getRealName(), inUserNumber.getText(), 33333,
                            seldata.getRealSex(), seldata.getImage());
                    Cform.setVisible(true);
                    dispose();

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    startTiplabel.setText("µ«¬Ω ß∞‹£¨Œ¥÷™¥ÌŒÛ£°");
                    startLoadingImage.setIcon(null);
                    startLoadingImage.setOpaque(false);
                    startTiplabel.setForeground(Color.RED);
                    System.out.println("login error!");
                    e1.printStackTrace();
                }

            } else {
                System.out.println("login error!");
                startLoadingImage.setIcon(null);
                startLoadingImage.setOpaque(false);
                startTiplabel.setText("µ«¬Ω ß∞‹£¨’À∫≈/√‹¬Î¥ÌŒÛ£°");
                startTiplabel.setForeground(Color.RED);
            }
        } else {
            System.out.println("login error!");
            startLoadingImage.setIcon(null);
            startLoadingImage.setOpaque(false);
            startTiplabel.setText("µ«¬Ω ß∞‹£¨’À∫≈/√‹¬Î¥ÌŒÛ£°");
            startTiplabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel.class.getName());
                    StartForm frame = new StartForm();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
