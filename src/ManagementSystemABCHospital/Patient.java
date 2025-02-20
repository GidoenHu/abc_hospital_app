package ManagementSystemABCHospital;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Patient extends JFrame {
    // Patient data fields
    private String id;
    private int age;
    private String name;
    private BloodInfo bloodInfo;

    // UI components
    private static DefaultTableModel patientTableModel;
    private static JTable patientTable;
    private static JLabel statusLabel;
    private JPanel formPanel;
    private JTextField idField, ageField, nameField;
    private JComboBox<String> bloodTypeCombo, rhFactorCombo;

    // Constructors
    public Patient() {
        // Default values
        this.id = "1000";
        this.age = 30;
        this.name = "Default Patient";
        this.bloodInfo = new BloodInfo("O", "+");

        // Initialize UI
        initUI();
    }

    public Patient(String id, int age, String name, BloodInfo bloodInfo) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.bloodInfo = bloodInfo;

        // Initialize UI
        initUI();
    }

    // Private UI initialization
    private void initUI() {
        // 1. Setup frame
        UIHelper.setupFrame(this, "Patient Management System");

        // 2. Create background panel
        JPanel backgroundPanel = UIHelper.createGradientBackground(new Color(100, 130, 170), new Color(190, 230, 240));
        backgroundPanel.setLayout(new BorderLayout(10, 10));
        this.setContentPane(backgroundPanel);

        // 3. Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Patient Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 51, 102));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel("Ready to manage patient data", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(80, 80, 80));
        titlePanel.add(statusLabel, BorderLayout.SOUTH);

        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        // 4. Create tabbed pane for different views
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // 5. Create table panel
        JPanel tablePanel = createTablePanel();
        tabbedPane.addTab("Patient List", new ImageIcon(), tablePanel, "View all patients");

        // 6. Create form panel for adding/editing patients
        formPanel = createFormPanel();
        tabbedPane.addTab("Add/Edit Patient", new ImageIcon(), formPanel, "Add or edit patient information");

        backgroundPanel.add(tabbedPane, BorderLayout.CENTER);

        // 7. Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Add sample data button
        JButton sampleDataButton = new JButton("Add Sample Data");
        sampleDataButton.setFont(new Font("Arial", Font.BOLD, 14));
        sampleDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSampleData();
            }
        });
        buttonPanel.add(sampleDataButton);

        // Return button
        JButton backButton = UIHelper.createBackButton(this);
        buttonPanel.add(backButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set window size and visibility
        setSize(800, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Create table panel to display patients
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create table
        String[] columnNames = {"ID", "Age", "Name", "Blood Type", "Rh Factor"};
        patientTableModel = new DefaultTableModel(columnNames, 0);
        patientTable = new JTable(patientTableModel);
        patientTable.setFont(new Font("Arial", Font.PLAIN, 14));
        patientTable.setRowHeight(25);
        patientTable.setGridColor(new Color(200, 200, 200));
        patientTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        patientTable.getTableHeader().setBackground(new Color(80, 120, 160));
        patientTable.getTableHeader().setForeground(Color.WHITE);

        // Center content in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < patientTable.getColumnCount(); i++) {
            patientTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(80, 120, 160), 1)
        ));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons for table operations
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tableButtonPanel.setOpaque(false);

        JButton editButton = new JButton("Edit Selected");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedPatient();
            }
        });

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedPatient();
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTableData();
            }
        });

        tableButtonPanel.add(editButton);
        tableButtonPanel.add(deleteButton);
        tableButtonPanel.add(refreshButton);

        panel.add(tableButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Create form panel for adding/editing patients
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form fields
        JPanel formFieldsPanel = new JPanel(new GridLayout(5, 2, 10, 20));
        formFieldsPanel.setOpaque(false);

        // ID
        formFieldsPanel.add(new JLabel("Patient ID:"));
        idField = new JTextField(id);
        formFieldsPanel.add(idField);

        // Age
        formFieldsPanel.add(new JLabel("Age:"));
        ageField = new JTextField(String.valueOf(age));
        formFieldsPanel.add(ageField);

        // Name
        formFieldsPanel.add(new JLabel("Name:"));
        nameField = new JTextField(name);
        formFieldsPanel.add(nameField);

        // Blood Type
        formFieldsPanel.add(new JLabel("Blood Type:"));
        bloodTypeCombo = new JComboBox<>(new String[]{"A", "B", "AB", "O"});
        bloodTypeCombo.setSelectedItem(bloodInfo.getBloodType());
        formFieldsPanel.add(bloodTypeCombo);

        // Rh Factor
        formFieldsPanel.add(new JLabel("Rh Factor:"));
        rhFactorCombo = new JComboBox<>(new String[]{"+", "-"});
        rhFactorCombo.setSelectedItem(bloodInfo.getRhFactor());
        formFieldsPanel.add(rhFactorCombo);

        // Add to panel with some padding
        JPanel paddedFormPanel = new JPanel(new BorderLayout());
        paddedFormPanel.setOpaque(false);
        paddedFormPanel.add(formFieldsPanel, BorderLayout.NORTH);
        panel.add(paddedFormPanel, BorderLayout.CENTER);

        // Form buttons
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        formButtonPanel.setOpaque(false);

        JButton saveButton = new JButton("Save Patient");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePatientData();
            }
        });

        JButton clearButton = new JButton("Clear Form");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        formButtonPanel.add(saveButton);
        formButtonPanel.add(clearButton);

        panel.add(formButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Add sample patient data to the table
    private void addSampleData() {
        patientTableModel.setRowCount(0); // Clear current data

        // Add sample patients
        addPatientToTable(new Patient("1001", 45, "John Smith", new BloodInfo("A", "+")));
        addPatientToTable(new Patient("1002", 32, "Maria Garcia", new BloodInfo("O", "-")));
        addPatientToTable(new Patient("1003", 67, "James Wilson", new BloodInfo("B", "+")));
        addPatientToTable(new Patient("1004", 29, "Sarah Johnson", new BloodInfo("AB", "+")));
        addPatientToTable(new Patient("1005", 51, "Robert Lee", new BloodInfo("A", "-")));

        updateStatus("Added 5 sample patients to database");
    }

    // Add a single patient to the table
    private void addPatientToTable(Patient patient) {
        Object[] rowData = {
                patient.getId(),
                patient.getAge(),
                patient.getName(),
                patient.getBloodInfo().getBloodType(),
                patient.getBloodInfo().getRhFactor()
        };
        patientTableModel.addRow(rowData);
    }

    // Save patient data from form
    private void savePatientData() {
        try {
            // Get data from form
            String id = idField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String name = nameField.getText().trim();
            String bloodType = (String) bloodTypeCombo.getSelectedItem();
            String rhFactor = (String) rhFactorCombo.getSelectedItem();

            // Validate data
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "ID and Name fields cannot be empty",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create patient object
            Patient patient = new Patient(id, age, name, new BloodInfo(bloodType, rhFactor));

            // Check if patient with this ID already exists
            boolean updated = false;
            for (int i = 0; i < patientTableModel.getRowCount(); i++) {
                if (patientTableModel.getValueAt(i, 0).equals(id)) {
                    // Update existing row
                    patientTableModel.setValueAt(age, i, 1);
                    patientTableModel.setValueAt(name, i, 2);
                    patientTableModel.setValueAt(bloodType, i, 3);
                    patientTableModel.setValueAt(rhFactor, i, 4);
                    updated = true;
                    break;
                }
            }

            // If not found, add new row
            if (!updated) {
                addPatientToTable(patient);
            }

            updateStatus(updated ? "Updated patient ID: " + id : "Added new patient ID: " + id);
            JOptionPane.showMessageDialog(this,
                    "Patient data saved successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear form after successful save
            clearForm();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Age must be a valid number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving patient data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit the selected patient
    private void editSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a patient to edit",
                    "Selection Required",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Get data from selected row
        String id = (String) patientTableModel.getValueAt(selectedRow, 0);
        int age = (int) patientTableModel.getValueAt(selectedRow, 1);
        String name = (String) patientTableModel.getValueAt(selectedRow, 2);
        String bloodType = (String) patientTableModel.getValueAt(selectedRow, 3);
        String rhFactor = (String) patientTableModel.getValueAt(selectedRow, 4);

        // Populate form fields
        idField.setText(id);
        ageField.setText(String.valueOf(age));
        nameField.setText(name);
        bloodTypeCombo.setSelectedItem(bloodType);
        rhFactorCombo.setSelectedItem(rhFactor);

        // Switch to form tab
        JTabbedPane tabbedPane = (JTabbedPane) formPanel.getParent();
        tabbedPane.setSelectedComponent(formPanel);

        updateStatus("Editing patient ID: " + id);
    }

    // Delete the selected patient
    private void deleteSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a patient to delete",
                    "Selection Required",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String id = (String) patientTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete patient with ID: " + id + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            patientTableModel.removeRow(selectedRow);
            updateStatus("Deleted patient ID: " + id);
        }
    }

    // Clear form fields
    private void clearForm() {
        idField.setText("");
        ageField.setText("");
        nameField.setText("");
        bloodTypeCombo.setSelectedIndex(0);
        rhFactorCombo.setSelectedIndex(0);
    }

    // Refresh table data (for external updates)
    private void refreshTableData() {
        JOptionPane.showMessageDialog(this,
                "Table data refreshed",
                "Refresh Complete",
                JOptionPane.INFORMATION_MESSAGE);
        updateStatus("Data refreshed: " + getCurrentTime());
    }

    // Update status message
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message + " - " + getCurrentTime());
        }
    }

    // Get current time formatted
    private static String getCurrentTime() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
    }

    // Getters and setters for patient data
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BloodInfo getBloodInfo() {
        return bloodInfo;
    }

    public void setBloodInfo(BloodInfo bloodInfo) {
        this.bloodInfo = bloodInfo;
    }

    // Static method to get the patient table model for TestPatient class to use
    public static DefaultTableModel getPatientTableModel() {
        return patientTableModel;
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Patient();
            }
        });
    }
}

