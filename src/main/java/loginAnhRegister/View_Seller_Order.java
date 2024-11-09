package loginAnhRegister;

import Model.Order;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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
        setTitle("Danh sách đơn hàng");
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
        emailComboBox.addItem("Tất cả");
        HashSet<String> emails = new HashSet<>();
        for (Order order : orders) {
            emails.add(order.getEmail());
        }
        for (String email : emails) {
            emailComboBox.addItem(email);
        }

        // Nút "Lọc" để thực hiện lọc email
        JButton filterButton = new JButton("Lọc");
        filterButton.setFont(new Font("Arial", Font.BOLD, 14));
        filterButton.setBackground(new Color(100, 149, 237));
        filterButton.setForeground(Color.WHITE);
        filterButton.addActionListener(e -> filterOrdersByEmail());

        filterPanel.add(new JLabel("Chọn Email:"));
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

        // Nút xác nhận đơn hàng
        JButton confirmButton = new JButton("Xác nhận đơn hàng");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(34, 139, 34));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setPreferredSize(new Dimension(200, 40));
        confirmButton.addActionListener(e -> confirmSelectedOrders());

        // Nút xuất hóa đơn (tạm thời chưa xử lý sự kiện)
        JButton exportInvoiceButton = new JButton("Xuất hóa đơn");
        exportInvoiceButton.setFont(new Font("Arial", Font.BOLD, 14));
        exportInvoiceButton.setBackground(new Color(70, 130, 180));
        exportInvoiceButton.setForeground(Color.WHITE);
        exportInvoiceButton.setPreferredSize(new Dimension(200, 40));
        exportInvoiceButton.addActionListener(e -> {});

        // Nút quay lại (tạm thời chưa xử lý sự kiện)
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(192, 192, 192));
        backButton.setForeground(Color.BLACK);
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> {});

        buttonPanel.add(confirmButton);
        buttonPanel.add(exportInvoiceButton);
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
        tableModel.setRowCount(0); // Xóa các dòng cũ

        for (Order order : orders) {
            if ("Tất cả".equals(selectedEmail) || order.getEmail().equals(selectedEmail)) {
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một đơn hàng để xác nhận.");
            return;
        }

        // Duyệt qua các dòng đã chọn và xác nhận đơn hàng
        for (int i = selectedRows.length - 1; i >= 0; i--) { // Duyệt ngược để tránh lỗi khi xóa hàng
            int row = selectedRows[i];
            String status = (String) tableModel.getValueAt(row, 8);

            // Kiểm tra trạng thái trước khi xác nhận
            if (!"Confirmed".equals(status)) {
                tableModel.setValueAt("Confirmed", row, 8);
                orders.get(row).setStatus("Confirmed");
                moveOrderToHistory(orders.get(row)); // Di chuyển đơn hàng sang file lịch sử
                tableModel.removeRow(row); // Xóa dòng đã xác nhận khỏi bảng
                orders.remove(orders.get(row));
                CsvFileHandler.writeOrdersToUnconfirmOrders(orders);
            }
        }

        JOptionPane.showMessageDialog(this, "Các đơn hàng đã được xác nhận và chuyển vào lịch sử.");
    }

    // Phương thức di chuyển đơn hàng sang file lịch sử sau khi xác nhận
    private void moveOrderToHistory(Order order) {
        String fileName = "orderHistory/" + order.getEmail().replace("@gmail.com", "") + ".csv";
        File orderFile = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFile, true))) {
            // Viết tiêu đề nếu file trống
            if (orderFile.length() == 0) {
                writer.write("Email,Phone ID,Brand,Model,Price,Quantity,Purchase Time,Discount,Status");
                writer.newLine();
            }

            // Ghi dữ liệu đơn hàng vào file
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
