package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerManagementGUI extends JDialog {
    private JTextField idField, nameField, addressField;
    private JRadioButton maleButton, femaleButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerDatabase customerDatabase;

    public CustomerManagementGUI(CustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
        setTitle("Data Pelanggan");
        setSize(600, 400);
        setLayout(new BorderLayout(10, 10)); // Memberikan jarak antar komponen
        setLocationRelativeTo(null);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Kode Pelanggan
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Kode Pelanggan:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        idField = new JTextField(20);
        idField.setPreferredSize(new Dimension(300,30));
        formPanel.add(idField, gbc);

        // Nama
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(300,30));
        formPanel.add(nameField, gbc);

        // Jenis Kelamin
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Jenis Kelamin:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maleButton = new JRadioButton("Laki-Laki");
        femaleButton = new JRadioButton("Perempuan");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        formPanel.add(genderPanel, gbc);

        // Alamat
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        addressField = new JTextField(20);
        addressField.setPreferredSize(new Dimension(300,30));
        formPanel.add(addressField, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Tabel Pelanggan
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Gender", "Alamat"}, 0);
        customerTable = new JTable(tableModel);
        loadCustomersToTable();
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Panel Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Tambah");
        JButton updateButton = new JButton("Ubah");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());

        setVisible(true);
    }

    private void loadCustomersToTable() {
        tableModel.setRowCount(0);
        for (Customer customer : customerDatabase.getCustomers()) {
            tableModel.addRow(new Object[]{customer.getId(), customer.getName(), customer.getGender(), customer.getAddress()});
        }
    }

    private void addCustomer() {
        String id = idField.getText();
        String name = nameField.getText();
        String gender = maleButton.isSelected() ? "L" : "P";
        String address = addressField.getText();

        Customer customer = new Customer(id, name, gender, address);
        customerDatabase.addCustomer(customer);
        loadCustomersToTable();
        clearFields();
    }

    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) { // Jika tidak ada baris yang dipilih
            JOptionPane.showMessageDialog(this, "Harap pilih pelanggan yang ingin diubah!", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Menghentikan eksekusi metode
        }

        String id = idField.getText();
        String name = nameField.getText();
        String gender = maleButton.isSelected() ? "L" : "P";
        String address = addressField.getText();

        if (id.isEmpty() || name.isEmpty() || address.isEmpty()) { // Validasi input kosong
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buat objek customer baru berdasarkan input
        Customer customer = new Customer(id, name, gender, address);

        // Update data di database dan tabel
        customerDatabase.updateCustomer(selectedRow, customer);
        loadCustomersToTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }


    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            customerDatabase.deleteCustomer(selectedRow);
            loadCustomersToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
    }
}