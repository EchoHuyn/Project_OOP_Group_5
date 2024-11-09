package Seller;

import Model.Order;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class View_Seller_Order extends JFrame {

    private JTable orderTable;
    private DefaultTableModel tableModel;
    private ArrayList<Order> orders;
    private JComboBox<String> emailComboBox;

    public View_Seller_Order(ArrayList<Order> orders) {
        this.orders = orders;
        setTitle("Order List");
        setSize(1400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // Thiết lập bố cục và các thành phần
        setLayout(new BorderLayout());
        createEmailFilter(); // Tạo bộ lọc email
        createTable(); // Tạo bảng hiển thị đơn hàng
        createButtons(); // Tạo các nút thao tác
    }

    // Phương thức tạo bộ lọc email
    private void createEmailFilter() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(220, 220, 220));

        emailComboBox = new JComboBox<>();
        emailComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        // Thêm các email vào JComboBox để người dùng chọn lọc
        emailComboBox.addItem("All");
        HashSet<String> emails = new HashSet<>();
        for (Order order : orders) {
            emails.add(order.getEmail());
        }
        for (String email : emails) {
            emailComboBox.addItem(email);
        }

        // Nút "Lọc" để thực hiện lọc email
        JButton filterButton = new JButton("Filter");
        filterButton.setFont(new Font("Arial", Font.BOLD, 14));
        filterButton.setBackground(new Color(100, 149, 237));
        filterButton.setForeground(Color.WHITE);
        filterButton.addActionListener(e -> filterOrdersByEmail());

        filterPanel.add(new JLabel("Select Email:"));
        filterPanel.add(emailComboBox);
        filterPanel.add(filterButton);
        add(filterPanel, BorderLayout.NORTH);
    }

    // Phương thức tạo bảng hiển thị đơn hàng
    private void createTable() {
        String[] columnNames = {"Email", "Phone ID", "Brand", "Model", "Price", "Quantity", "Purchase Time", "Discount", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Arial", Font.PLAIN, 14));
        orderTable.setRowHeight(25);
        orderTable.setGridColor(Color.LIGHT_GRAY);
        orderTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Cho phép chọn nhiều dòng

        JTableHeader header = orderTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(60, 179, 113));
        header.setForeground(Color.WHITE);

        loadOrdersToTable(); // Tải dữ liệu đơn hàng vào bảng

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Phương thức tạo các nút thao tác
    private void createButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Confirm orders button
        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(34, 139, 34));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setPreferredSize(new Dimension(200, 40));
        confirmButton.addActionListener(e -> confirmSelectedOrders());

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(192, 192, 192));
        backButton.setForeground(Color.BLACK);
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_Seller.display();
                dispose();
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Phương thức tải dữ liệu đơn hàng vào bảng
    private void loadOrdersToTable() {
        tableModel.setRowCount(0); // Xóa các dòng cũ
        for (Order order : orders) {
            Object[] row = {
                    order.getEmail(),
                    order.getPhones().getPhoneId(),
                    order.getPhones().getBrand(),
                    order.getPhones().getModel(),
                    order.getPhones().getPrice(),
                    order.getPhones().getStockQuantity(),
                    order.getPurchaseTime(),
                    order.getDiscount(),
                    order.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    // Phương thức lọc đơn hàng theo email
    private void filterOrdersByEmail() {
        String selectedEmail = (String) emailComboBox.getSelectedItem();
        tableModel.setRowCount(0); // Clear old rows

        for (Order order : orders) {
            if ("All".equals(selectedEmail) || order.getEmail().equals(selectedEmail)) {
                Object[] row = {
                        order.getEmail(),
                        order.getPhones().getPhoneId(),
                        order.getPhones().getBrand(),
                        order.getPhones().getModel(),
                        order.getPhones().getPrice(),
                        order.getPhones().getStockQuantity(),
                        order.getPurchaseTime(),
                        order.getDiscount(),
                        order.getStatus()
                };
                tableModel.addRow(row);
            }
        }
    }

    // Phương thức xác nhận các đơn hàng đã chọn
    private void confirmSelectedOrders() {
        int[] selectedRows = orderTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one order to confirm.");
            return;
        }

        // Iterate over selected rows and confirm each order
        for (int i = selectedRows.length - 1; i >= 0; i--) { // Reverse iteration to avoid deletion issues
            int row = selectedRows[i];
            String status = (String) tableModel.getValueAt(row, 8);

            // Check the status before confirming
            if (!"Confirmed".equals(status)) {
                tableModel.setValueAt("Confirmed", row, 8);
                orders.get(row).setStatus("Confirmed");
                moveOrderToHistory(orders.get(row)); // Move the order to history file
                tableModel.removeRow(row); // Remove the confirmed row from the table
                orders.remove(orders.get(row));
                CsvFileHandler.writeOrdersToUnconfirmOrders(orders);
            }
        }

        JOptionPane.showMessageDialog(this, "Orders have been confirmed and moved to history.");
    }

    // Phương thức di chuyển đơn hàng sang file lịch sử sau khi xác nhận
    private void moveOrderToHistory(Order order) {
        String fileName = "orderHistory/" + order.getEmail().replace("@gmail.com", "") + ".csv";
        File orderFile = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFile, true))) {
            // Write header if the file is empty
            if (orderFile.length() == 0) {
                writer.write("Email,Phone ID,Brand,Model,Price,Quantity,Purchase Time,Discount,Status");
                writer.newLine();
            }

            // Write order data to the file
            writer.write(
                    order.getPhones().getPhoneId() + ","
                    + order.getPhones().getBrand() + ","
                    + order.getPhones().getModel() + ","
                    + order.getPhones().getPrice() + ","
                    + order.getPhones().getStockQuantity() + ","
                    + order.getPurchaseTime() + ","
                    + order.getDiscount() + ","
                    + order.getStatus());
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức hiển thị giao diện chính của chương trình
    public static void display(ArrayList<Order> orders) {
        SwingUtilities.invokeLater(() -> {
            View_Seller_Order view = new View_Seller_Order(orders);
            view.setVisible(true);
        });
    }

    // Phương thức chính
    public static void display() {
        ArrayList<Order> orders = CsvFileHandler.readOrdersFromCSV(); // Đọc dữ liệu từ file CSV
        display(orders); // Hiển thị giao diện chính
    }
}
