package ManagementSystemABCHospital;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class MedicalEquipment extends JFrame {
    private JTable equipmentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton addButton, editButton, deleteButton;
    private final Map<String, EquipmentData> equipmentDatabase = new HashMap<>();

    // Model class for equipment data
    private static class EquipmentData {
        String name;
        String type;
        String location;
        String status;
        String lastMaintenance;

        public EquipmentData(String name, String type, String location, String status, String lastMaintenance) {
            this.name = name;
            this.type = type;
            this.location = location;
            this.status = status;
            this.lastMaintenance = lastMaintenance;
        }
    }

    public MedicalEquipment() {
        // Set up the frame
        UIHelper.setupFrame(this, "Medical Equipment Management");

        // Initialize sample data
        initializeSampleData();

        // Add components
        initComponents();

        // Add event listeners
        setupEventListeners();
    }

    private void initializeSampleData() {
        // Add sample equipment data
        equipmentDatabase.put("ST001", new EquipmentData("Stethoscope", "Diagnostic", "Ward A", "Available", "2024-12-15"));
        equipmentDatabase.put("TM002", new EquipmentData("Thermometer", "Diagnostic", "Emergency", "In Use", "2024-11-20"));
        equipmentDatabase.put("BP003", new EquipmentData("Blood Pressure Monitor", "Monitoring", "ICU", "Available", "2025-01-10"));
        equipmentDatabase.put("ECG004", new EquipmentData("ECG Machine", "Monitoring", "Cardiology", "Under Maintenance", "2024-09-05"));
        equipmentDatabase.put("US005", new EquipmentData("Ultrasound Scanner", "Imaging", "Radiology", "Available", "2024-10-12"));
    }

    private void initComponents() {
        // *Background panel (gradient color)*
        JPanel backgroundPanel = UIHelper.createGradientBackground(new Color(150, 102, 193), new Color(230, 248, 255));
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        this.setContentPane(backgroundPanel);

        // *Top control panel*
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));
        controlPanel.setLayout(new BorderLayout(5, 10)); // 使用BorderLayout，并设置组件间间距

        // Search and filter components
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchFilterPanel.setOpaque(false);

        searchField = new JTextField(15);
        searchField.setToolTipText("Search by equipment ID or name");

        String[] filterOptions = {"All Types", "Diagnostic", "Monitoring", "Imaging", "Therapeutic"};
        filterComboBox = new JComboBox<>(filterOptions);

        JLabel searchLabel = new JLabel("Search:");
        JLabel filterLabel = new JLabel("Filter by Type:");

        searchFilterPanel.add(searchLabel);
        searchFilterPanel.add(searchField);
        searchFilterPanel.add(Box.createHorizontalStrut(20));
        searchFilterPanel.add(filterLabel);
        searchFilterPanel.add(filterComboBox);

        // 创建一个新的面板来包含按钮，并添加上边距
        JPanel buttonPanelContainer = new JPanel(new BorderLayout());
        buttonPanelContainer.setOpaque(false);
        buttonPanelContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 添加20像素的上边距

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        addButton = new JButton("Add New");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 将按钮面板添加到容器中
        buttonPanelContainer.add(buttonPanel, BorderLayout.CENTER);

        // 添加到控制面板：搜索在上，按钮在下
        controlPanel.add(searchFilterPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanelContainer, BorderLayout.SOUTH);

        // *Table for displaying equipment*
        String[] columnNames = {"ID", "Equipment Name", "Type", "Location", "Status", "Last Maintenance"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        // Populate table with data
        for (Map.Entry<String, EquipmentData> entry : equipmentDatabase.entrySet()) {
            String id = entry.getKey();
            EquipmentData data = entry.getValue();
            tableModel.addRow(new Object[]{
                    id,
                    data.name,
                    data.type,
                    data.location,
                    data.status,
                    data.lastMaintenance
            });
        }

        equipmentTable = new JTable(tableModel);
        equipmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        equipmentTable.setRowHeight(25);
        equipmentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        // Custom renderer for status column
        equipmentTable.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(equipmentTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 10, 10, 10),
                BorderFactory.createLineBorder(new Color(120, 82, 173), 1)
        ));

        // *Summary panel at bottom*
        JPanel summaryPanel = createSummaryPanel();

        // Add components to the main panel
        backgroundPanel.add(controlPanel, BorderLayout.NORTH);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.add(summaryPanel, BorderLayout.SOUTH);

        // *Back button*
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navigationPanel.setOpaque(false);
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        JButton backButton = UIHelper.createBackButton(this);
        navigationPanel.add(backButton);

        backgroundPanel.add(navigationPanel, BorderLayout.PAGE_END);
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(1, 4, 10, 0));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summaryPanel.setOpaque(false);

        // Count items by status
        int available = 0, inUse = 0, maintenance = 0;
        for (EquipmentData data : equipmentDatabase.values()) {
            switch (data.status) {
                case "Available": available++; break;
                case "In Use": inUse++; break;
                case "Under Maintenance": maintenance++; break;
            }
        }

        // Create summary cards
        summaryPanel.add(createSummaryCard("Total Equipment", String.valueOf(equipmentDatabase.size()), new Color(150, 102, 193, 80)));
        summaryPanel.add(createSummaryCard("Available", String.valueOf(available), new Color(100, 200, 100, 80)));
        summaryPanel.add(createSummaryCard("In Use", String.valueOf(inUse), new Color(255, 180, 0, 80)));
        summaryPanel.add(createSummaryCard("Under Maintenance", String.valueOf(maintenance), new Color(255, 100, 100, 80)));

        return summaryPanel;
    }

    private JPanel createSummaryCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void setupEventListeners() {
        // Search functionality
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });

        // Filter dropdown
        filterComboBox.addActionListener(e -> filterTable());

        // Add button
        addButton.addActionListener(e -> showAddEquipmentDialog());

        // Edit button
        editButton.addActionListener(e -> {
            int selectedRow = equipmentTable.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                showEditEquipmentDialog(id);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select an equipment to edit",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Delete button
        deleteButton.addActionListener(e -> {
            int selectedRow = equipmentTable.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                confirmAndDeleteEquipment(id, selectedRow);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select an equipment to delete",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Double click on table row
        equipmentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = equipmentTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String id = (String) tableModel.getValueAt(selectedRow, 0);
                        showEquipmentDetailsDialog(id);
                    }
                }
            }
        });
    }

    private void filterTable() {
        String searchText = searchField.getText().toLowerCase();
        String selectedType = (String) filterComboBox.getSelectedItem();

        tableModel.setRowCount(0); // Clear table

        for (Map.Entry<String, EquipmentData> entry : equipmentDatabase.entrySet()) {
            String id = entry.getKey();
            EquipmentData data = entry.getValue();

            boolean matchesSearch = searchText.isEmpty() ||
                    id.toLowerCase().contains(searchText) ||
                    data.name.toLowerCase().contains(searchText);

            boolean matchesFilter = "All Types".equals(selectedType) ||
                    data.type.equals(selectedType);

            if (matchesSearch && matchesFilter) {
                tableModel.addRow(new Object[]{
                        id,
                        data.name,
                        data.type,
                        data.location,
                        data.status,
                        data.lastMaintenance
                });
            }
        }
    }

    // Dialog methods
    private void showAddEquipmentDialog() {
        // Implementation for adding new equipment
        JOptionPane.showMessageDialog(this, "Add Equipment Dialog would appear here",
                "Add Equipment", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEditEquipmentDialog(String equipmentId) {
        // Implementation for editing equipment
        JOptionPane.showMessageDialog(this, "Edit Dialog for Equipment ID: " + equipmentId,
                "Edit Equipment", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showEquipmentDetailsDialog(String equipmentId) {
        // Implementation for showing detailed information
        JOptionPane.showMessageDialog(this, "Details for Equipment ID: " + equipmentId,
                "Equipment Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmAndDeleteEquipment(String id, int rowIndex) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete equipment: " + id + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            equipmentDatabase.remove(id);
            tableModel.removeRow(rowIndex);
            JOptionPane.showMessageDialog(this,
                    "Equipment deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Custom renderer for status column
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String status = value.toString();
                switch (status) {
                    case "Available":
                        cell.setForeground(new Color(0, 150, 0));
                        break;
                    case "In Use":
                        cell.setForeground(new Color(200, 150, 0));
                        break;
                    case "Under Maintenance":
                        cell.setForeground(new Color(200, 0, 0));
                        break;
                    default:
                        cell.setForeground(Color.BLACK);
                }
            }

            return cell;
        }
    }
}

