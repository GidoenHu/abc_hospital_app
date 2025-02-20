package ManagementSystemABCHospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Index extends JFrame implements ActionListener {
    JMenuItem BloodData = new JMenuItem("Blood Data");
    JMenuItem TestBloodData = new JMenuItem("Test Blood Data");
    JMenuItem Patient = new JMenuItem("Patient");
    JMenuItem TestPatient = new JMenuItem("Test Patient");
    JMenuItem Medicine = new JMenuItem("Medicine");
    JMenuItem MedicalEquipment = new JMenuItem("Medical Equipment");
    JMenuItem FollowUs = new JMenuItem("Follow Us");

    private JButton loginButton;
    private JButton registerButton;

    public Index() {
        // 设置窗口属性
        initFrame();

        // 初始化菜单栏
        initMenu();

        // 添加界面组件
        initComponents();

        setVisible(true);
    }

    private void initFrame() {
        this.setSize(430, 632);  // 适配 iPhone 16 Pro Max
        this.setTitle("ABC Hospital Management System");
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu functionMenu = new JMenu("Function");
        JMenu aboutUsMenu = new JMenu("About Us");

        // 添加动作监听器
        BloodData.addActionListener(this);
        TestBloodData.addActionListener(this);
        Patient.addActionListener(this);
        TestPatient.addActionListener(this);
        Medicine.addActionListener(this);
        MedicalEquipment.addActionListener(this);
        FollowUs.addActionListener(this);

        functionMenu.add(BloodData);
        functionMenu.add(TestBloodData);
        functionMenu.add(Patient);
        functionMenu.add(TestPatient);
        functionMenu.add(Medicine);
        functionMenu.add(MedicalEquipment);
        aboutUsMenu.add(FollowUs);

        menuBar.add(functionMenu);
        menuBar.add(aboutUsMenu);
        this.setJMenuBar(menuBar);
    }

    private void initComponents() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(173, 216, 230),
                        0, getHeight(), new Color(255, 250, 250));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setBounds(0, 0, 430, 932);
        backgroundPanel.setLayout(null);
        this.setContentPane(backgroundPanel);

        // **标题**
        JLabel titleLabel = new JLabel("Welcome to ABC Hospital", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBounds(30, 50, 370, 40);
        backgroundPanel.add(titleLabel);

        // **简介**
        JLabel profileLabel = new JLabel("<html><center>"
                + "<b>Providing Quality Healthcare</b><br>"
                + "We ensure the best medical services with cutting-edge technology and experienced professionals.<br>"
                + "Your health, our priority."
                + "</center></html>", SwingConstants.CENTER);
        profileLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        profileLabel.setForeground(new Color(47, 79, 79));
        profileLabel.setBounds(30, 100, 370, 100);
        backgroundPanel.add(profileLabel);

        // **用户名**
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setBounds(50, 250, 100, 30);
        backgroundPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setBounds(160, 250, 200, 30);
        backgroundPanel.add(usernameField);

        // **密码**
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setBounds(50, 300, 100, 30);
        backgroundPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setBounds(160, 300, 200, 30);
        backgroundPanel.add(passwordField);

        // **登录按钮**
        loginButton = new JButton("Login");
        styleButton(loginButton, 50, 370, 140, 40, new Color(72, 209, 204));
        loginButton.addActionListener(this);
        backgroundPanel.add(loginButton);

        // **注册按钮**
        registerButton = new JButton("Register");
        styleButton(registerButton, 220, 370, 140, 40, new Color(255, 69, 0));
        registerButton.addActionListener(this);
        backgroundPanel.add(registerButton);
    }

    private void styleButton(JButton button, int x, int y, int width, int height, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBounds(x, y, width, height);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        button.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            JOptionPane.showMessageDialog(this, "Login successful! Redirecting to patient page...");
        } else if (e.getSource() == registerButton) {
            JOptionPane.showMessageDialog(this, "Only doctors can register an account for patients.");
        } else if (e.getSource() == BloodData) {
            openNewFrame(new BloodData());
        } else if (e.getSource() == TestBloodData) {
            openNewFrame(new TestBloodData());
        } else if (e.getSource() == Patient) {
            openNewFrame(new Patient());
        } else if (e.getSource() == TestPatient) {
            openNewFrame(new TestPatient());
        } else if (e.getSource() == Medicine) {
            openNewFrame(new Medicine());
        } else if (e.getSource() == MedicalEquipment) {
            openNewFrame(new MedicalEquipment());
        } else if (e.getSource() == FollowUs) {
            JOptionPane.showMessageDialog(this, "Follow us on social media!");
        }
    }

    // 打开新窗口并关闭当前窗口
    private void openNewFrame(JFrame newFrame) {
        newFrame.setVisible(true);
        this.dispose(); // 关闭当前窗口
    }
}



