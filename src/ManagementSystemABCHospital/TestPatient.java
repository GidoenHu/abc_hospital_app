package ManagementSystemABCHospital;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestPatient extends JFrame {
    private static DefaultTableModel patientTableModel;
    private static JTable patientTable;
    private static JLabel statusLabel;

    public TestPatient() {
        initUI();
    }

    private void initUI() {
        // 1. 设置窗口
        UIHelper.setupFrame(this, "Test Patient System");

        // 2. 创建背景面板
        JPanel backgroundPanel = UIHelper.createGradientBackground(new Color(120, 152, 193), new Color(210, 248, 255));
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        this.setContentPane(backgroundPanel);

        // 3. 标题面板
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Test Patient Data Monitor", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // 状态标签
        statusLabel = new JLabel("Waiting for patient data updates...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(80, 80, 80));
        titlePanel.add(statusLabel, BorderLayout.SOUTH);

        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // 4. 创建表格
        String[] columnNames = {"ID", "Age", "Name", "Blood Type", "Rh Factor"};
        patientTableModel = new DefaultTableModel(columnNames, 0);
        patientTable = new JTable(patientTableModel);
        patientTable.setFont(new Font("Arial", Font.PLAIN, 14));
        patientTable.setRowHeight(25);
        patientTable.setGridColor(new Color(200, 200, 200));
        patientTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        patientTable.getTableHeader().setBackground(new Color(100, 150, 180));
        patientTable.getTableHeader().setForeground(Color.WHITE);

        // 设置表格内容居中
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < patientTable.getColumnCount(); i++) {
            patientTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(new Color(100, 150, 180), 1)
        ));
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // 5. 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // 刷新按钮
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Patient.getPatientTableModel() != null) {
                    updatePatientData(Patient.getPatientTableModel());
                    JOptionPane.showMessageDialog(TestPatient.this,
                            "Data refreshed from Patient system", "Refresh Complete",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(TestPatient.this,
                            "No data available from Patient system", "Refresh Failed",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(refreshButton);

        // 显示示例按钮
        JButton showSampleButton = new JButton("Show Sample Patient");
        showSampleButton.setFont(new Font("Arial", Font.BOLD, 14));
        showSampleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSamplePatient();
            }
        });
        buttonPanel.add(showSampleButton);

        // 返回按钮
        JButton backButton = UIHelper.createBackButton(this);
        buttonPanel.add(backButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 设置窗口大小和可见性
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 显示示例患者
    private void showSamplePatient() {
        // 创建示例患者并显示其信息
        Patient defaultPatient = new Patient();
        Patient customPatient = new Patient("5000", 35, "Example Patient", new BloodInfo("B", "+"));

        // 创建信息面板
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 10));

        JLabel defaultLabel = new JLabel("<html><b>Default Patient:</b> ID=" + defaultPatient.getId() +
                ", Age=" + defaultPatient.getAge() +
                ", Name=" + defaultPatient.getName() +
                ", Blood Data=" + defaultPatient.getBloodInfo().getBloodType() +
                defaultPatient.getBloodInfo().getRhFactor() + "</html>");

        JLabel customLabel = new JLabel("<html><b>Custom Patient:</b> ID=" + customPatient.getId() +
                ", Age=" + customPatient.getAge() +
                ", Name=" + customPatient.getName() +
                ", Blood Data=" + customPatient.getBloodInfo().getBloodType() +
                customPatient.getBloodInfo().getRhFactor() + "</html>");

        // Update the default patient
        defaultPatient.setId("9999");
        defaultPatient.setAge(42);
        defaultPatient.setName("Updated Patient");
        defaultPatient.setBloodInfo(new BloodInfo("AB", "-"));

        JLabel updatedLabel = new JLabel("<html><b>Updated Patient:</b> ID=" + defaultPatient.getId() +
                ", Age=" + defaultPatient.getAge() +
                ", Name=" + defaultPatient.getName() +
                ", Blood Data=" + defaultPatient.getBloodInfo().getBloodType() +
                defaultPatient.getBloodInfo().getRhFactor() + "</html>");

        infoPanel.add(defaultLabel);
        infoPanel.add(customLabel);
        infoPanel.add(updatedLabel);

        JOptionPane.showMessageDialog(this, infoPanel, "Sample Patient Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // 静态方法用于更新患者数据
    public static void updatePatientData(DefaultTableModel newTableModel) {
        if (patientTableModel == null || patientTable == null) {
            return; // 防止在UI初始化前调用
        }

        // 清空当前数据
        patientTableModel.setRowCount(0);

        // 复制新数据
        for (int i = 0; i < newTableModel.getRowCount(); i++) {
            Object[] rowData = new Object[newTableModel.getColumnCount()];
            for (int j = 0; j < newTableModel.getColumnCount(); j++) {
                rowData[j] = newTableModel.getValueAt(i, j);
            }
            patientTableModel.addRow(rowData);
        }

        // 更新状态消息
        if (statusLabel != null) {
            statusLabel.setText("Data updated: " + getCurrentTime() + " (" +
                    patientTableModel.getRowCount() + " patients)");
        }

        // 通知表格数据已更新
        patientTableModel.fireTableDataChanged();

        // 如果窗口不可见，则显示窗口
        Window window = SwingUtilities.getWindowAncestor(patientTable);
        if (window instanceof JFrame && !window.isVisible()) {
            window.setVisible(true);
        }
    }

    // 获取当前时间
    private static String getCurrentTime() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
    }
}
