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

        setLayout(new GridLayout(8, 2));
        setSize(400, 300);

        add(new JLabel("Title:"));
        titleField = new JTextField(isUpdate ? book.getTitle() : "");
        add(titleField);

        add(new JLabel("Author:"));
        authorField = new JTextField(isUpdate ? book.getAuthor() : "");
        add(authorField);

        add(new JLabel("Publisher:"));
        publisherField = new JTextField(isUpdate ? book.getPublisher() : "");
        add(publisherField);

        add(new JLabel("Year:"));
        yearField = new JTextField(isUpdate ? String.valueOf(book.getYear()) : "");
        add(yearField);

        add(new JLabel("Stock:"));
        stockField = new JTextField(isUpdate ? String.valueOf(book.getStock()) : "");
        add(stockField);

        add(new JLabel("Price:"));
        priceField = new JTextField(isUpdate ? String.valueOf(book.getPrice()) : "");
        add(priceField);

        add(new JLabel("Image:"));
        imageLabel = new JLabel(isUpdate ? book.getImagePath() : "No Image", SwingConstants.CENTER);
        add(imageLabel);

        JButton uploadButton = new JButton("Upload Image");
        add(uploadButton);

        JButton saveButton = new JButton(isUpdate ? "Update" : "Add");
        add(saveButton);

        uploadButton.addActionListener(e -> uploadImage());
        saveButton.addActionListener(e -> saveBook());

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