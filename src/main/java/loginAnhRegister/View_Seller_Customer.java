package loginAnhRegister;

import Model.Customer;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class View_Seller_Customer extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public View_Seller_Customer(ArrayList<Customer> customers) {
        setTitle("Customer Information");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tiêu đề các cột
        String[] columnNames = {"Name", "Gender", "BirthDate", "Hometown", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Thêm dữ liệu vào bảng
        for (Customer customer : customers) {
            Object[] rowData = {
                customer.getName(),
                customer.getGender(),
                customer.getBirthDate(),
                customer.getHometown(),
                customer.getEmail()
            };
            tableModel.addRow(rowData);
        }

        // Tạo bảng với mô hình dữ liệu
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Đặt bảng vào JScrollPane để có thanh cuộn
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private static ArrayList <Customer> customers;
    
    public static void main(String[] args) {
        customers = CsvFileHandler.readCustomersFromCSV("accountCustomers.csv");

        // Hiển thị giao diện
        SwingUtilities.invokeLater(() -> {
            new View_Seller_Customer(customers).setVisible(true);
        });
    }
}
