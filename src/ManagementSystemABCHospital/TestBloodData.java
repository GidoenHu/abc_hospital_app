package ManagementSystemABCHospital;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TestBloodData extends JFrame {
    private static JTable bloodTable;
    private static DefaultTableModel tableModel;

    public TestBloodData() {
        initUI();
    }

    private void initUI() {
        // 1. 设置窗口
        UIHelper.setupFrame(this, "Test Blood Data");

        // 2. 创建背景面板
        JPanel backgroundPanel = UIHelper.createGradientBackground(new Color(255, 182, 193), new Color(180, 248, 255));
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(backgroundPanel);

        // 3. 添加标题面板
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel titleLabel = new JLabel("Test Blood Data", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(139, 0, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // 4. 创建状态标签
        JLabel statusLabel = new JLabel("Waiting for data from Blood Data module...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(100, 100, 100));

        // 5. 创建表格
        String[] columnNames = {"Blood Type", "Stock (Units)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bloodTable = new JTable(tableModel);
        bloodTable.setFont(new Font("Arial", Font.PLAIN, 16));
        bloodTable.setRowHeight(30);

        // 设置单元格居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < bloodTable.getColumnCount(); i++) {
            bloodTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(bloodTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // 6. 底部面板（包含状态和返回按钮）
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        JButton backButton = UIHelper.createBackButton(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 7. 设置窗口尺寸和位置
        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // 更新表格数据
    public static void updateTableData(DefaultTableModel newTableModel) {
        if (tableModel == null || bloodTable == null) {
            return; // 防止在UI初始化之前调用
        }

        // 清空旧数据
        tableModel.setRowCount(0);

        // 遍历新表格模型并添加到当前表格
        for (int i = 0; i < newTableModel.getRowCount(); i++) {
            Object[] rowData = new Object[newTableModel.getColumnCount()];
            for (int j = 0; j < newTableModel.getColumnCount(); j++) {
                rowData[j] = newTableModel.getValueAt(i, j);
            }
            tableModel.addRow(rowData);
        }

        // 应用表格渲染器以确保新添加的行也居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < bloodTable.getColumnCount(); i++) {
            bloodTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // 通知表格数据已更新
        tableModel.fireTableDataChanged();

        // 如果这是窗口不可见的状态下收到的更新，则显示窗口
        Window window = SwingUtilities.getWindowAncestor(bloodTable);
        if (window instanceof JFrame && !window.isVisible()) {
            window.setVisible(true);
        }
    }
}

