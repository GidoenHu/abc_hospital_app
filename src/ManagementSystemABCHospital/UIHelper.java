package ManagementSystemABCHospital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIHelper {

    // 设置窗口基本属性
    public static void setupFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
    }

    // 创建渐变背景面板
    public static JPanel createGradientBackground(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    // 创建返回按钮
    public static JButton createBackButton(JFrame currentFrame) {
        JButton backButton = new JButton("Back to Index");
        backButton.setBounds(120, 500, 150, 40);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.dispose(); // 关闭当前窗口
                new Index().setVisible(true); // 打开主页面
            }
        });
        return backButton;
    }
}
