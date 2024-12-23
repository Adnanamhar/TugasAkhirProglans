package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BookStoreGUI extends JFrame {
    BookDatabase bookDatabase;
    private TransactionDatabase transactionDatabase;
    public CustomerDatabase customerDatabase; // Ubah menjadi public agar bisa diakses
    private Cart cart; // Tambahkan keranjang
    JTable bookTable;
    private DefaultTableModel tableModel;

    public BookStoreGUI() {
        bookDatabase = new BookDatabase();
        transactionDatabase = new TransactionDatabase();
        customerDatabase = new CustomerDatabase(); // Inisialisasi di sini
        cart = new Cart(); // Inisialisasi keranjang
        setTitle("Toko Buku");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        tableModel = new DefaultTableModel(new String[]{"Title", "Author", "Publisher", "Year", "Stock", "Price", "Image"}, 0);
        bookTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return ImageIcon.class; // Kolom gambar
                }
                return super.getColumnClass(columnIndex);
            }
        };
        bookTable.setRowHeight(100); // Set tinggi baris untuk gambar
        loadBooksToTable();
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        JButton updateButton = new JButton("Update Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton transactionButton = new JButton("Transactions");
        JButton reportButton = new JButton("Sales Report");
        JButton customerButton = new JButton("Data Pelanggan"); // Tombol untuk data pelanggan
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(transactionButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(customerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> new BookManagementGUI(this, "Add Book", null));
        updateButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                new BookManagementGUI(this, "Update Book", bookDatabase.getBooks().get(selectedRow));
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to update!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> deleteBook());
        transactionButton.addActionListener(e -> new TransactionGUI(this));
        reportButton.addActionListener(e -> new SalesReportGUI(this));
        customerButton.addActionListener(e -> new CustomerManagementGUI(customerDatabase)); // Action listener untuk pelanggan

        setVisible(true);
    }

    public void loadBooksToTable() {
        tableModel.setRowCount(0);
        for (Book book : bookDatabase.getBooks()) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(book.getImagePath()).getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH)); // Resize image
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getPublisher(), book.getYear(), book.getStock(), book.getPrice(), imageIcon});
        }
    }

    public void addBook(Book book) {
        bookDatabase.addBook(book);
        loadBooksToTable();
    }

    public void updateBook(int index, Book book) {
        bookDatabase.updateBook(index, book);
        loadBooksToTable();
    }

    public void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            bookDatabase.deleteBook(selectedRow);
            loadBooksToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Select a book to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public TransactionDatabase getTransactionDatabase() {
        return transactionDatabase;
    }

    public Cart getCart() {
        return cart; // Tambahkan metode untuk mengakses keranjang
    }

    public DefaultTableModel getBookTableModel() {
        return tableModel; // Tambahkan metode untuk mengakses model tabel buku
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookStoreGUI::new);
    }
}
