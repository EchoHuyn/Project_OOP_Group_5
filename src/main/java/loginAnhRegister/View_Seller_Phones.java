package loginAnhRegister;

import Model.Phones;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class View_Seller_Phones extends JFrame {

    private JTable phoneTable;
    private DefaultTableModel tableModel;
    private JButton addPhoneButton, deletePhoneButton, editPhoneButton, backButton;
    private ArrayList<Phones> phoneList;

    public View_Seller_Phones() {
        setTitle("Seller Phone Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background and font colors
        getContentPane().setBackground(new Color(240, 248, 255));

        // Load phone data from CSV
        phoneList = CsvFileHandler.readPhonesFromCSV("phones.csv");

        // Column headers for phone attributes
        String[] columnNames = {"Phone ID", "Brand", "Model", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        phoneTable = new JTable(tableModel);
        phoneTable.setRowHeight(25);
        phoneTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        phoneTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        phoneTable.getTableHeader().setBackground(new Color(100, 149, 237));
        phoneTable.getTableHeader().setForeground(Color.WHITE);

        // Load phone data into the table
        loadPhoneDataToTable(phoneList);

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(phoneTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create buttons with design
        addPhoneButton = new JButton("Add Phone");
        addPhoneButton.setFont(new Font("Arial", Font.BOLD, 14));
        addPhoneButton.setBackground(new Color(60, 179, 113));
        addPhoneButton.setForeground(Color.WHITE);

        deletePhoneButton = new JButton("Delete Phone");
        deletePhoneButton.setFont(new Font("Arial", Font.BOLD, 14));
        deletePhoneButton.setBackground(new Color(220, 53, 69));
        deletePhoneButton.setForeground(Color.WHITE);

        editPhoneButton = new JButton("Edit Phone");
        editPhoneButton.setFont(new Font("Arial", Font.BOLD, 14));
        editPhoneButton.setBackground(new Color(255, 165, 0));
        editPhoneButton.setForeground(Color.WHITE);

        // Create "Back" button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(addPhoneButton);
        buttonPanel.add(deletePhoneButton);
        buttonPanel.add(editPhoneButton);
        buttonPanel.add(backButton);

        // Add scrollPane and buttonPanel to frame
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        addPhoneButton.addActionListener(e -> addPhone());
        deletePhoneButton.addActionListener(e -> deletePhone());
        editPhoneButton.addActionListener(e -> editPhone());

        //Xử lý sự kiện cho nút Back
        backButton.addActionListener(e -> {
            View_Seller.display();
            dispose();
        });
    }

    private void loadPhoneDataToTable(ArrayList<Phones> phones) {
        tableModel.setRowCount(0);
        for (Phones phone : phones) {
            Object[] rowData = {
                phone.getPhoneId(),
                phone.getBrand(),
                phone.getModel(),
                phone.getPrice(),
                phone.getStockQuantity()
            };
            tableModel.addRow(rowData);
        }
    }

    private void addPhone() {
        JTextField idField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        Object[] message = {
            "Phone ID:", idField,
            "Brand:", brandField,
            "Model:", modelField,
            "Price:", priceField,
            "Stock Quantity:", stockField,};

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Phone", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newPhoneId = idField.getText().trim();
            String brand = brandField.getText().trim().toUpperCase();
            String model = modelField.getText().trim().toUpperCase();
            String price = priceField.getText().trim();
            String stockQuantity = stockField.getText().trim();

            // Check for empty fields
            if (newPhoneId.isEmpty() || brand.isEmpty() || model.isEmpty() || price.isEmpty() || stockQuantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check for duplicate PhoneID
            boolean isDuplicate = phoneList.stream().anyMatch(phone -> phone.getPhoneId().equals(newPhoneId));
            if (isDuplicate) {
                JOptionPane.showMessageDialog(this, "Error: Phone ID already exists.", "Duplicate Phone ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate numeric fields and handle leading zeros and invalid values
            try {
                price = removeLeadingZeros(price);
                stockQuantity = removeLeadingZeros(stockQuantity);

                double priceValue = Double.parseDouble(price);
                int stockValue = Integer.parseInt(stockQuantity);

                if (priceValue <= 0 || stockValue <= 0) {
                    JOptionPane.showMessageDialog(this, "Price and Stock Quantity must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Price and Stock Quantity must be positive numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add the new phone
            try {
                Phones newPhone = new Phones(newPhoneId, brand, model, price, stockQuantity);
                phoneList.add(newPhone);
                CsvFileHandler.writePhonesToCSV(phoneList, "phones.csv");
                loadPhoneDataToTable(phoneList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding phone. Please check inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editPhone() {
        int selectedRow = phoneTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a phone to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String phoneId = (String) tableModel.getValueAt(selectedRow, 0);
        Phones phoneToEdit = phoneList.stream().filter(phone -> phone.getPhoneId().equals(phoneId)).findFirst().orElse(null);

        if (phoneToEdit == null) {
            JOptionPane.showMessageDialog(this, "Phone not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField brandField = new JTextField(phoneToEdit.getBrand());
        JTextField modelField = new JTextField(phoneToEdit.getModel());
        JTextField priceField = new JTextField(phoneToEdit.getPrice());
        JTextField stockField = new JTextField(phoneToEdit.getStockQuantity());

        Object[] message = {
            "Brand:", brandField,
            "Model:", modelField,
            "Price:", priceField,
            "Stock Quantity:", stockField,};

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Phone", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String brand = brandField.getText().trim().toUpperCase();
            String model = modelField.getText().trim().toUpperCase();
            String price = priceField.getText().trim();
            String stockQuantity = stockField.getText().trim();

            // Check for empty fields
            if (brand.isEmpty() || model.isEmpty() || price.isEmpty() || stockQuantity.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate numeric fields and handle leading zeros and invalid values
            try {
                price = removeLeadingZeros(price);
                stockQuantity = removeLeadingZeros(stockQuantity);

                double priceValue = Double.parseDouble(price);
                int stockValue = Integer.parseInt(stockQuantity);

                if (priceValue <= 0 || stockValue <= 0) {
                    JOptionPane.showMessageDialog(this, "Price and Stock Quantity must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Price and Stock Quantity must be positive numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update phone details
            try {
                phoneToEdit.setBrand(brand);
                phoneToEdit.setModel(model);
                phoneToEdit.setPrice(price);
                phoneToEdit.setStockQuantity(stockQuantity);
                CsvFileHandler.writePhonesToCSV(phoneList, "phones.csv");
                loadPhoneDataToTable(phoneList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error editing phone. Please check inputs.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// Helper method to remove leading zeros from a numeric string
    private String removeLeadingZeros(String value) {
        return value.replaceFirst("^0+(?!$)", "");
    }

    private void deletePhone() {
        int selectedRow = phoneTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a phone to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String phoneId = (String) tableModel.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this, "Delete entire entry or by quantity?", "Delete Phone", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            phoneList.removeIf(phone -> phone.getPhoneId().equals(phoneId));
        } else {
            String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity to delete:");
            int quantityToDelete = Integer.parseInt(quantityStr);

            for (Phones phone : phoneList) {
                if (phone.getPhoneId().equals(phoneId)) {
                    int currentStock = Integer.parseInt(phone.getStockQuantity());
                    phone.setStockQuantity(String.valueOf(currentStock - quantityToDelete));
                    break;
                }
            }
        }
        CsvFileHandler.writePhonesToCSV(phoneList, "phones.csv");
        loadPhoneDataToTable(phoneList);
    }

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            new View_Seller_Phones().setVisible(true);
        });
    }
}
