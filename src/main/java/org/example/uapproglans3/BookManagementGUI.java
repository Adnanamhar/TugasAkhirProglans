package org.example.uapproglans3;

import javax.swing.*;
import java.awt.*;

public class BookManagementGUI extends JDialog {
    private JTextField titleField, authorField, publisherField, yearField, stockField, priceField;
    private JLabel imageLabel;
    private BookStoreGUI parent;
    private Book book;
    private boolean isUpdate;

    public BookManagementGUI(BookStoreGUI parent, String title, Book book) {
        super(parent, title, true);
        this.parent = parent;
        this.book = book;
        this.isUpdate = book != null;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margin antar elemen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        titleField = new JTextField(isUpdate ? book.getTitle() : "");
        titleField.setPreferredSize(new Dimension(300, 30));
        add(titleField, gbc);

        // Author
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        authorField = new JTextField(isUpdate ? book.getAuthor() : "");
        authorField.setPreferredSize(new Dimension(300, 30));
        add(authorField, gbc);

        // Publisher
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Publisher:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        publisherField = new JTextField(isUpdate ? book.getPublisher() : "", 40);
        publisherField.setPreferredSize(new Dimension(300, 30));
        add(publisherField, gbc);

        // Year
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Year:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        yearField = new JTextField(isUpdate ? String.valueOf(book.getYear()) : "", 40);
        yearField.setPreferredSize(new Dimension(300, 30));
        add(yearField, gbc);

        // Stock
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        stockField = new JTextField(isUpdate ? String.valueOf(book.getStock()) : "", 40);
        stockField.setPreferredSize(new Dimension(300, 30));
        add(stockField, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        priceField = new JTextField(isUpdate ? String.valueOf(book.getPrice()) : "", 40);
        priceField.setPreferredSize(new Dimension(300, 30));
        add(priceField, gbc);

        // Image Label
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Image:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        imageLabel = new JLabel(isUpdate ? book.getImagePath() : "No Image", SwingConstants.CENTER);
        add(imageLabel, gbc);

        // Upload Button
        gbc.gridx = 1; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        JButton uploadButton = new JButton("Upload Image");
        add(uploadButton, gbc);

        // Save Button
        gbc.gridx = 1; gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton(isUpdate ? "Update" : "Add");
        add(saveButton, gbc);

        // Tambahkan Listener
        uploadButton.addActionListener(e -> uploadImage());
        saveButton.addActionListener(e -> saveBook());

        setSize(600, 400);
        setLocationRelativeTo(parent); // Center the dialog relative to the parent
        setVisible(true);
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imageLabel.setText(imagePath);
        }
    }

    private void saveBook() {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String publisher = publisherField.getText();
            int year = Integer.parseInt(yearField.getText());
            int stock = Integer.parseInt(stockField.getText());
            double price = Double.parseDouble(priceField.getText());
            String imagePath = imageLabel.getText();

            Book newBook = new Book(title, author, publisher, year, stock, imagePath, price);
            if (isUpdate) {
                parent.updateBook(parent.bookTable.getSelectedRow(), newBook);
            } else {
                parent.addBook(newBook);
            }
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(BookManagementGUI.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
