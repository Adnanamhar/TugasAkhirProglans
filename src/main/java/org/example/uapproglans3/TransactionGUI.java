package org.example.uapproglans3;

import javax.swing.*;
import java.awt.*;

public class TransactionGUI extends JDialog {
    private JTextField transactionCodeField, bookCodeField, quantityField;
    private JComboBox<String> customerComboBox; // Dropdown untuk pelanggan
    private BookStoreGUI parent;

    public TransactionGUI(BookStoreGUI parent) {
        super(parent, "Transaksi", true);
        this.parent = parent;

        setLayout(new GridLayout(5, 2));
        setSize(400, 300);

        // Kode Transaksi otomatis
        transactionCodeField = new JTextField("TRANS" + System.currentTimeMillis());
        transactionCodeField.setEditable(false);
        add(new JLabel("Kode Transaksi:"));
        add(transactionCodeField);

        add(new JLabel("Kode Pelanggan:"));
        customerComboBox = new JComboBox<>();
        loadCustomersToComboBox(); // Load customers into the combo box
        add(customerComboBox);

        add(new JLabel("Kode Buku:"));
        bookCodeField = new JTextField();
        bookCodeField.setEditable(false); // Kode buku tidak bisa diedit
        add(bookCodeField);

        add(new JLabel("Jumlah:"));
        quantityField = new JTextField();
        add(quantityField);

        JButton addToCartButton = new JButton("Tambah ke Keranjang");
        add(addToCartButton);

        JButton viewCartButton = new JButton("Lihat Keranjang");
        add(viewCartButton);

        // Tabel Buku
        JTable bookTable = new JTable(parent.getBookTableModel());
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = bookTable.getSelectedRow();
                if (row != -1) {
                    String bookCode = (String) bookTable.getValueAt(row, 0); // Ambil kode buku dari tabel
                    bookCodeField.setText(bookCode); // Set kode buku ke field
                }
            }
        });
        add(new JScrollPane(bookTable));

        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());

        setVisible(true);
    }

    private void loadCustomersToComboBox() {
        for (Customer customer : parent.customerDatabase.getCustomers()) {
            customerComboBox.addItem(customer.getId() + " - " + customer.getName());
        }
    }

    private void addToCart() {
        String transactionCode = transactionCodeField.getText();
        String selectedCustomer = (String) customerComboBox.getSelectedItem();
        String customerId = selectedCustomer.split(" - ")[0]; // Ambil ID pelanggan
        String bookCode = bookCodeField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        // Ambil informasi buku dari database
        for (Book book : parent.bookDatabase.getBooks()) {
            if (book.getTitle().equals(bookCode)) {
                CartItem item = new CartItem(book.getTitle(), book.getTitle(), quantity, book.getPrice());
                parent.getCart().addItem(item); // Tambahkan item ke keranjang
                JOptionPane.showMessageDialog(this, "Buku ditambahkan ke keranjang!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void viewCart() {
        new CartGUI(parent.getCart(), parent, customerComboBox.getSelectedItem().toString()); // Tampilkan keranjang dengan ID pelanggan
    }
}
