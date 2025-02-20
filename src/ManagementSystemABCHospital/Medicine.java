package ManagementSystemABCHospital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

public class Medicine extends JFrame {
    // Components
    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JTextField nameField, dosageField, priceField;
    private JComboBox<String> categoryCombo;
    private JSpinner quantitySpinner;
    private JTextArea descriptionArea;

    JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

    public Medicine() {
        // Set up the frame
        UIHelper.setupFrame(this, "Medicine Management System");

        // Add components
        initComponents();
    }

    private void initComponents() {
        // Create gradient background panel
        JPanel backgroundPanel = UIHelper.createGradientBackground(
                new Color(120, 12, 170), new Color(220, 248, 255)
        );
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        this.setContentPane(backgroundPanel);

        // Header panel with title and search
        JPanel headerPanel = createHeaderPanel();
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);

        // Main content with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Left panel - Medicine table
        JPanel tablePanel = createTablePanel();
        splitPane.setLeftComponent(tablePanel);

        // Right panel - Medicine details/editor
        JPanel detailsPanel = createDetailsPanel();
        splitPane.setRightComponent(detailsPanel);

        backgroundPanel.add(splitPane, BorderLayout.CENTER);

        // Footer panel with buttons
        JPanel footerPanel = createFooterPanel();
        backgroundPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add sample data
        addSampleData();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5)); // 使用 BorderLayout 布局
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        // Title with icon
        JLabel titleLabel = new JLabel("Medicine Inventory Management");
        titleLabel.setFont(new Font("Segue UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Create a simple pill icon using Unicode
        JLabel iconLabel = new JLabel("\uD83D\uDC8A"); // Pill emoji
        iconLabel.setFont(new Font("Segue UI", Font.PLAIN, 30));
        iconLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // 将标题面板放在左侧
        panel.add(titlePanel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchField = new JTextField(15); // 设置搜索框的宽度

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterMedicineList();
            }
        });

        JLabel filterLabel = new JLabel("Category:");
        filterLabel.setForeground(Color.WHITE);
        categoryFilter = new JComboBox<>(new String[]{"All", "Analgesic", "Antibiotic", "Antihistamine", "Antiviral"});
        categoryFilter.addActionListener(e -> filterMedicineList());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(filterLabel);
        searchPanel.add(categoryFilter);

        // 将搜索面板放在右侧
        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Create table model with columns
        String[] columns = {"ID", "Medicine Name", "Category", "Dosage", "Quantity", "Price ($)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        medicineTable = new JTable(tableModel);
        medicineTable.setRowHeight(30);
        medicineTable.setFont(new Font("Segue UI", Font.PLAIN, 14));
        medicineTable.getTableHeader().setFont(new Font("Segue UI", Font.BOLD, 14));
        medicineTable.getTableHeader().setBackground(new Color(100, 50, 150));
        medicineTable.getTableHeader().setForeground(Color.WHITE);
        medicineTable.setSelectionBackground(new Color(220, 220, 250));
        medicineTable.setGridColor(new Color(220, 220, 240));

        // Add selection listener
        medicineTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && medicineTable.getSelectedRow() != -1) {
                displayMedicineDetails(medicineTable.getSelectedRow());
            }
        });

        JScrollPane scrollPane = new JScrollPane(medicineTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Stats panel

        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel totalLabel = new JLabel("Total Medicines: 0");
        totalLabel.setFont(new Font("Segue UI", Font.BOLD, 14));

        JLabel lowStockLabel = new JLabel("Low Stock: 0");
        lowStockLabel.setFont(new Font("Segue UI", Font.BOLD, 14));
        lowStockLabel.setForeground(Color.RED);

        statsPanel.add(totalLabel);
        statsPanel.add(lowStockLabel);

        panel.add(statsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(new Color(255, 255, 255, 180));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Title
        JLabel detailsTitle = new JLabel("Medicine Details");
        detailsTitle.setFont(new Font("Segue UI", Font.BOLD, 18));
        detailsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        detailsTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(detailsTitle, BorderLayout.NORTH);

        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createFormLabel("Medicine Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Category combobox
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(createFormLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        categoryCombo = new JComboBox<>(new String[]{"Analgesic", "Antibiotic", "Antihistamine", "Antiviral", "Other"});
        formPanel.add(categoryCombo, gbc);

        // Dosage field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(createFormLabel("Dosage:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dosageField = new JTextField(20);
        formPanel.add(dosageField, gbc);

        // Quantity spinner
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createFormLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 10000, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        formPanel.add(quantitySpinner, gbc);

        // Price field
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createFormLabel("Price ($):"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        priceField = new JTextField(20);
        formPanel.add(priceField, gbc);

        // Description area
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(createFormLabel("Description:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descScrollPane, gbc);

        // Action buttons
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        actionPanel.setOpaque(false);

        JButton saveButton = createStyledButton("Save", new Color(0, 150, 50));
        saveButton.addActionListener(e -> saveMedicine());

        JButton newButton = createStyledButton("New", new Color(0, 100, 200));
        newButton.addActionListener(e -> clearForm());

        JButton deleteButton = createStyledButton("Delete", new Color(200, 50, 50));
        deleteButton.addActionListener(e -> deleteMedicine());

        actionPanel.add(newButton);
        actionPanel.add(saveButton);
        actionPanel.add(deleteButton);

        formPanel.add(actionPanel, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        // Status panel on the left
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setOpaque(false);

        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setForeground(Color.WHITE);
        statusPanel.add(statusLabel);

        panel.add(statusPanel, BorderLayout.WEST);

        // Buttons panel on the right
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton printButton = new JButton("Print Report");
        printButton.setFont(new Font("Segue UI", Font.BOLD, 14));
        printButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Report generated and sent to printer", "Print Report",
                JOptionPane.INFORMATION_MESSAGE));

        JButton backButton = UIHelper.createBackButton(this);
        backButton.setFont(new Font("Segue UI", Font.BOLD, 14));

        buttonsPanel.add(printButton);
        buttonsPanel.add(backButton);

        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segue UI", Font.BOLD, 14));
        return label;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segue UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 30));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void addSampleData() {
        // Add sample medicines
        Object[][] sampleData = {
                {"M001", "Aspirin", "Analgesic", "500mg", 120, 5.99},
                {"M002", "Ibuprofen", "Analgesic", "200mg", 80, 7.50},
                {"M003", "Paracetamol", "Analgesic", "650mg", 150, 4.25},
                {"M004", "Amoxicillin", "Antibiotic", "250mg", 45, 12.99},
                {"M005", "Containerize", "Antihistamine", "10mg", 60, 9.75},
                {"M006", "Loratadine", "Antihistamine", "5mg", 30, 14.50},
                {"M007", "Erythromycin", "Antibiotic", "500mg", 20, 25.99},
                {"M008", "Avitaminosis", "Antiviral", "75mg", 10, 45.00}
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }

        // Update stats
        updateStats();
    }

    private void filterMedicineList() {
        String searchText = searchField.getText().toLowerCase();
        String categoryText = (String) categoryFilter.getSelectedItem();

        DefaultTableModel defaultModel = (DefaultTableModel) medicineTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(defaultModel);
        medicineTable.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> rowFilter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                boolean nameMatch = entry.getStringValue(1).toLowerCase().contains(searchText);
                boolean categoryMatch = categoryText.equals("All") || entry.getStringValue(2).equals(categoryText);
                return nameMatch && categoryMatch;
            }
        };

        sorter.setRowFilter(rowFilter);
        updateStats();
    }

    private void displayMedicineDetails(int row) {
        // Map table index to model index in case of filtering
        int modelRow = medicineTable.convertRowIndexToModel(row);

        nameField.setText((String) tableModel.getValueAt(modelRow, 1));
        categoryCombo.setSelectedItem((String) tableModel.getValueAt(modelRow, 2));
        dosageField.setText((String) tableModel.getValueAt(modelRow, 3));
        quantitySpinner.setValue(tableModel.getValueAt(modelRow, 4));
        priceField.setText(String.valueOf(tableModel.getValueAt(modelRow, 5)));

        // Sample descriptions for medicines
        String[] descriptions = {
                "Used for pain relief, fever reduction, and as an anti-inflammatory. May cause stomach irritation.",
                "Non-steroidal anti-inflammatory drug (UNSAID) used for relieving pain, fever, and inflammation.",
                "Common pain reliever and fever reducer that works by blocking chemical messengers that transmit pain signals.",
                "Antibiotic used to treat bacterial infections. Take with food to reduce stomach upset.",
                "Second-generation antihistamine used to relieve allergy symptoms such as watery eyes, runny nose, itching, sneezing.",
                "Prescription-strength formula that helps reduce inflammation and provide relief from allergic reactions."
        };

        // Pick a random description for demonstration
        int descIndex = row % descriptions.length;
        descriptionArea.setText(descriptions[descIndex]);
    }

    private void clearForm() {
        nameField.setText("");
        categoryCombo.setSelectedIndex(0);
        dosageField.setText("");
        quantitySpinner.setValue(0);
        priceField.setText("");
        descriptionArea.setText("");
        medicineTable.clearSelection();
    }

    private void saveMedicine() {
        try {
            String name = nameField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String dosage = dosageField.getText().trim();
            int quantity = (int) quantitySpinner.getValue();
            double price = Double.parseDouble(priceField.getText().trim());

            if (name.isEmpty() || dosage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and dosage cannot be empty",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // If a row is selected, update it
            int selectedRow = medicineTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = medicineTable.convertRowIndexToModel(selectedRow);
                tableModel.setValueAt(name, modelRow, 1);
                tableModel.setValueAt(category, modelRow, 2);
                tableModel.setValueAt(dosage, modelRow, 3);
                tableModel.setValueAt(quantity, modelRow, 4);
                tableModel.setValueAt(price, modelRow, 5);

                JOptionPane.showMessageDialog(this, "Medicine updated successfully",
                        "Update Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Generate a new ID
                String newId = "M" + String.format("%03d", tableModel.getRowCount() + 1);

                // Add new row
                tableModel.addRow(new Object[]{
                        newId, name, category, dosage, quantity, price
                });

                JOptionPane.showMessageDialog(this, "New medicine added successfully",
                        "Add Success", JOptionPane.INFORMATION_MESSAGE);
            }

            // Clear form and update stats
            clearForm();
            updateStats();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMedicine() {
        int selectedRow = medicineTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a medicine to delete",
                    "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = medicineTable.convertRowIndexToModel(selectedRow);
        String medicineName = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete " + medicineName + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(modelRow);
            clearForm();
            updateStats();
            JOptionPane.showMessageDialog(this, medicineName + " has been deleted",
                    "Delete Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateStats() {
        int totalMedicines = tableModel.getRowCount();
        int lowStock = 0;

        // Count low stock items (less than 30)
        for (int i = 0; i < totalMedicines; i++) {
            int quantity = (int) tableModel.getValueAt(i, 4);
            if (quantity < 30) {
                lowStock++;
            }
        }

        // Update labels in the stats panel
        //Component[] components = ((JPanel)((JPanel)medicineTable.getParent().getParent()).getComponent(1)).getComponents();
        //((JLabel)components[0]).setText("Total Medicines: " + totalMedicines);
        //((JLabel)components[1]).setText("Low Stock: " + lowStock);

        if (statsPanel != null) {
            Component[] components = statsPanel.getComponents();
            if (components.length >= 2 && components[0] instanceof JLabel && components[1] instanceof JLabel) {
                ((JLabel) components[0]).setText("Total Medicines: " + totalMedicines);
                ((JLabel) components[1]).setText("Low Stock: " + lowStock);
            }
        }
    }
}





