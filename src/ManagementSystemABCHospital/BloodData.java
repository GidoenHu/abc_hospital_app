package ManagementSystemABCHospital;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BloodData extends JFrame {
    private JTable bloodInventoryTable;
    private JTable patientTable;
    private DefaultTableModel inventoryTableModel;
    private DefaultTableModel patientTableModel;

    public BloodData() {
        initUI();
    }

    private void initUI() {
        // 1. 设置窗口
        UIHelper.setupFrame(this, "Blood Data");

        // 2. 创建背景面板
        JPanel backgroundPanel = UIHelper.createGradientBackground(new Color(255, 182, 193), new Color(180, 248, 255));
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 3. 标题
        JLabel titleLabel = new JLabel("Blood Data", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(139, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(titleLabel, gbc);

        // 4. 血型库存数据表
        JLabel inventoryLabel = new JLabel("Blood Inventory", SwingConstants.CENTER);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inventoryLabel.setForeground(new Color(139, 0, 0));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        backgroundPanel.add(inventoryLabel, gbc);

        String[] inventoryColumnNames = {"Blood Type", "Stock (Units)"};
        Object[][] inventoryData = {
                {"A+", "120"}, {"A-", "80"}, {"B+", "100"}, {"B-", "60"},
                {"O+", "200"}, {"O-", "50"}, {"AB+", "90"}, {"AB-", "30"}
        };

        inventoryTableModel = new DefaultTableModel(inventoryData, inventoryColumnNames);
        bloodInventoryTable = new JTable(inventoryTableModel);
        bloodInventoryTable.setFont(new Font("Arial", Font.PLAIN, 16));
        bloodInventoryTable.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < bloodInventoryTable.getColumnCount(); i++) {
            bloodInventoryTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane inventoryScrollPane = new JScrollPane(bloodInventoryTable);
        inventoryScrollPane.setPreferredSize(new Dimension(350, 200));
        gbc.gridy = 2;
        backgroundPanel.add(inventoryScrollPane, gbc);

        // 5. 患者血型信息表
        JLabel patientLabel = new JLabel("Patient Blood Information", SwingConstants.CENTER);
        patientLabel.setFont(new Font("Arial", Font.BOLD, 18));
        patientLabel.setForeground(new Color(139, 0, 0));
        gbc.gridy = 3;
        backgroundPanel.add(patientLabel, gbc);

        String[] patientColumnNames = {"Name", "Blood Type", "Rh Factor"};
        Object[][] patientData = {
                {"Alex", "O", "Rh+"},
                {"Jordan", "A", "Rh-"},
                {"Taylor", "B", "Rh-"},
                {"Morgan", "AB", "Rh+"},
                {"Sam", "A", "Rh-"},
                {"Casey", "O", "Rh+"},
                {"Riley", "B", "Rh-"},
                {"Cameron", "AB", "Rh+"},
                {"Avery", "A", "Rh-"},
                {"Jamie", "O", "Rh+"}
        };

        patientTableModel = new DefaultTableModel(patientData, patientColumnNames);
        patientTable = new JTable(patientTableModel);
        patientTable.setFont(new Font("Arial", Font.PLAIN, 16));
        patientTable.setRowHeight(30);

        for (int i = 0; i < patientTable.getColumnCount(); i++) {
            patientTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane patientScrollPane = new JScrollPane(patientTable);
        patientScrollPane.setPreferredSize(new Dimension(350, 200));
        gbc.gridy = 4;
        backgroundPanel.add(patientScrollPane, gbc);

        // 6. 控制按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        // 更新库存按钮
        JButton updateInventoryButton = new JButton("Update Inventory");
        updateInventoryButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateInventoryButton.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(updateInventoryButton);

        updateInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBloodStock();
            }
        });

        // 更新患者信息按钮
        JButton updatePatientButton = new JButton("Update Patient Info");
        updatePatientButton.setFont(new Font("Arial", Font.BOLD, 16));
        updatePatientButton.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(updatePatientButton);

        updatePatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePatientInfo();
            }
        });

        gbc.gridy = 5;
        backgroundPanel.add(buttonPanel, gbc);

        // 7. 返回按钮
        JButton backButton = UIHelper.createBackButton(this);
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        backgroundPanel.add(backButton, gbc);

        // 首次启动时，立即同步数据到TestBloodData
        syncDataWithTestBloodData();

        pack(); // 调整窗口大小以适应内容
        setLocationRelativeTo(null); // 窗口居中
        setVisible(true);
    }

    // 更新血液库存
    private void updateBloodStock() {
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            int updatedStock = (int) (Math.random() * 200); // 随机更新库存
            inventoryTableModel.setValueAt(String.valueOf(updatedStock), i, 1);
        }

        // 同步数据到TestBloodData
        syncDataWithTestBloodData();
        JOptionPane.showMessageDialog(this, "Blood inventory updated successfully!");
    }

    // 更新患者信息
    private void updatePatientInfo() {
        // 这里可以实现更复杂的患者信息更新逻辑
        // 简单示例：随机调整某些患者的信息
        String[] rhFactors = {"Rh+", "Rh-"};
        String[] bloodTypes = {"A", "B", "AB", "O"};

        for (int i = 0; i < 3; i++) {
            int randomRow = (int)(Math.random() * patientTableModel.getRowCount());
            String randomBloodType = bloodTypes[(int)(Math.random() * bloodTypes.length)];
            String randomRhFactor = rhFactors[(int)(Math.random() * rhFactors.length)];

            patientTableModel.setValueAt(randomBloodType, randomRow, 1);
            patientTableModel.setValueAt(randomRhFactor, randomRow, 2);
        }

        // 同步数据到TestBloodData
        syncDataWithTestBloodData();
        JOptionPane.showMessageDialog(this, "Patient information updated successfully!");
    }

    // 同步数据到TestBloodData
    private void syncDataWithTestBloodData() {
        // 创建包含所有数据的复合模型
        DefaultTableModel combinedModel = new DefaultTableModel();

        // 添加库存表列
        for (int i = 0; i < inventoryTableModel.getColumnCount(); i++) {
            combinedModel.addColumn(inventoryTableModel.getColumnName(i));
        }

        // 添加库存数据行
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            Object[] rowData = new Object[inventoryTableModel.getColumnCount()];
            for (int j = 0; j < inventoryTableModel.getColumnCount(); j++) {
                rowData[j] = inventoryTableModel.getValueAt(i, j);
            }
            combinedModel.addRow(rowData);
        }

        // 分隔行
        Object[] separatorRow = new Object[inventoryTableModel.getColumnCount()];
        for (int i = 0; i < separatorRow.length; i++) {
            separatorRow[i] = "---------------";
        }
        combinedModel.addRow(separatorRow);

        // 添加患者表数据的标题行
        Object[] patientHeaderRow = new Object[inventoryTableModel.getColumnCount()];
        if (inventoryTableModel.getColumnCount() >= 2) {
            patientHeaderRow[0] = "Patient Name";
            patientHeaderRow[1] = "Blood Info";
        }
        combinedModel.addRow(patientHeaderRow);

        // 添加患者数据行
        for (int i = 0; i < patientTableModel.getRowCount(); i++) {
            Object[] rowData = new Object[inventoryTableModel.getColumnCount()];
            if (inventoryTableModel.getColumnCount() >= 2) {
                rowData[0] = patientTableModel.getValueAt(i, 0); // 患者姓名
                // 合并血型和Rh因子
                rowData[1] = patientTableModel.getValueAt(i, 1) + " " +
                        patientTableModel.getValueAt(i, 2);
            }
            combinedModel.addRow(rowData);
        }

        // 更新TestBloodData的表格
        TestBloodData.updateTableData(combinedModel);
    }

    // 获取最新库存数据
    public DefaultTableModel getInventoryTableModel() {
        return inventoryTableModel;
    }

    // 获取最新患者数据
    public DefaultTableModel getPatientTableModel() {
        return patientTableModel;
    }
}

