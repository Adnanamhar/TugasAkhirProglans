package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SalesReportGUI extends JDialog {
    private BookStoreGUI parent;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public SalesReportGUI(BookStoreGUI parent) {
        super(parent, "Laporan Penjualan", true);
        this.parent = parent;

        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(parent); // Center the dialog relative to the parent

        // Tabel untuk laporan penjualan
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Transaction ID", "Customer ID", "Book Title", "Quantity", "Subtotal"}, 0);
        reportTable = new JTable(tableModel);
        loadSalesReport();
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Tutup");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(closeButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dispose());
        deleteButton.addActionListener(e -> deleteSelectedRow());

        setVisible(true);
    }

    private void loadSalesReport() {
        for (Transaction transaction : parent.getTransactionDatabase().getTransactions()) {
            tableModel.addRow(new Object[]{
                    transaction.getOrderId(),
                    transaction.getTransactionId(),
                    transaction.getCustomerId(),
                    transaction.getBookTitle(), // Mengambil judul buku dari transaksi
                    transaction.getQuantity(),
                    transaction.getSubtotal()
            });
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = reportTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Konfirmasi penghapusan
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus baris ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Hapus dari model tabel
            String orderId = (String) tableModel.getValueAt(selectedRow, 0); // Ambil Order ID
            tableModel.removeRow(selectedRow);

            // Hapus dari database
            parent.getTransactionDatabase().removeTransactionByOrderId(orderId);
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
