package loginAnhRegister;

import Model.Customer;
import Model.Order;
import Model.Phones;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class View_Seller_Customer extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton viewTransactionHistoryButton;
    private JButton backButton;

    public View_Seller_Customer(ArrayList<Customer> customers) {
        setTitle("Customer Information");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thiết lập phông nền
        getContentPane().setBackground(new Color(240, 240, 240));

        // Tạo bảng tiêu đề các cột
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

        // Tạo bảng với mô hình dữ liệu và chỉnh sửa font, màu sắc
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(100, 149, 237));
        table.getTableHeader().setForeground(Color.WHITE);

        // Đặt bảng vào JScrollPane để có thanh cuộn
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo panel chứa các nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));

        // Nút View Transaction History
        viewTransactionHistoryButton = new JButton("View Transaction History");
        viewTransactionHistoryButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewTransactionHistoryButton.setBackground(new Color(60, 179, 113));
        viewTransactionHistoryButton.setForeground(Color.WHITE);
        viewTransactionHistoryButton.setPreferredSize(new Dimension(300, 40));

        // Nút Back
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(220, 53, 69));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 40));

        // Thêm các nút vào panel
        buttonPanel.add(viewTransactionHistoryButton);
        buttonPanel.add(backButton);

        // Thêm scrollPane và buttonPanel vào JFrame
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện nút "View Transaction History"
        viewTransactionHistoryButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer to view transaction history.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy email của khách hàng từ dòng được chọn và bỏ phần "@gmail.com"
            String email = ((String) tableModel.getValueAt(selectedRow, 4)).replace("@gmail.com", "");

            // Gọi hàm để lấy lịch sử giao dịch
            ArrayList<Order> transactionHistory = getTransactionHistory(email);

            // Kiểm tra nếu không có lịch sử giao dịch
            if (transactionHistory == null) {
                JOptionPane.showMessageDialog(this, "Transaction history file for the selected customer does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (transactionHistory.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No transaction history found for the selected customer.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Tạo một cửa sổ mới để hiển thị lịch sử giao dịch
            JFrame historyFrame = new JFrame("Transaction History - " + email);
            historyFrame.setSize(1200, 400);
            historyFrame.setLocationRelativeTo(this);
            historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Tạo bảng hiển thị lịch sử giao dịch
            String[] historyColumnNames = {"Phone ID", "Brand", "Model", "Price", "Quantity", "Order Date", "Discount"};
            DefaultTableModel historyTableModel = new DefaultTableModel(historyColumnNames, 0);
            JTable historyTable = new JTable(historyTableModel);
            historyTable.setRowHeight(25);
            historyTable.setFont(new Font("Arial", Font.PLAIN, 14));
            historyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

            // Thêm dữ liệu vào bảng lịch sử
            for (Order order : transactionHistory) {
                Object[] rowData = {
                    order.getPhones().getPhoneId(),
                    order.getPhones().getBrand(),
                    order.getPhones().getModel(),
                    order.getPhones().getPrice(),
                    order.getPhones().getStockQuantity(),
                    order.getPurchaseTime(),
                    order.getDiscount()
                };
                historyTableModel.addRow(rowData);
            }

            // Đặt bảng vào JScrollPane và thêm vào cửa sổ lịch sử
            JScrollPane historyScrollPane = new JScrollPane(historyTable);
            historyFrame.add(historyScrollPane, BorderLayout.CENTER);

            // Hiển thị cửa sổ lịch sử giao dịch
            historyFrame.setVisible(true);
        });

        backButton.addActionListener(e -> {
            // Xử lý sự kiện quay lại sẽ được thêm ở đây
            dispose();
        });
    }

    private ArrayList<Order> getTransactionHistory(String email) {
        ArrayList<Order> transactionHistory = new ArrayList<>();
        String filePath = "orderHistory/" + email + ".csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Bỏ qua dòng tiêu đề nếu có

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 7) { // Kiểm tra số lượng trường cần thiết
                    // Giả định cấu trúc dữ liệu: Phone ID, Brand, Model, Price, Quantity, Order Date, Discount
                    String phoneId = fields[0];
                    String brand = fields[1];
                    String model = fields[2];
                    String price = fields[3];
                    String quantity = fields[4];
                    String orderDate = fields[5];
                    String discount = fields[6];

                    // Tạo đối tượng Phones và Order
                    Phones phone = new Phones(phoneId, brand, model, price, quantity);
                    Order order = new Order(email, phone, orderDate, discount, "Confirmed");

                    // Thêm đối tượng Order vào danh sách
                    transactionHistory.add(order);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Transaction history file not found for email: " + email);
            return null; // Trả về null nếu tệp không tồn tại
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactionHistory;
    }

    private static ArrayList<Customer> customers;

    public static void display() {
        customers = CsvFileHandler.readCustomersFromCSV("accountCustomers.csv");

        // Hiển thị giao diện
        SwingUtilities.invokeLater(() -> {
            new View_Seller_Customer(customers).setVisible(true);
        });
    }
}
