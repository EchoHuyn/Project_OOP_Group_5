package loginAnhRegister;

import Model.Order;
import Model.Phones;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

public class View_Customer extends JFrame {

    private JTable phoneTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addToCartButton;
    private JButton searchButton;
    private JButton backButton;
    private JButton refreshButton;
    private JButton checkCartButton; // Nút kiểm tra giỏ hàng
    private JComboBox<String> filterComboBox;

    private ArrayList<Phones> phoneList;
    private String customerName;
    private String customerEmail;

    public View_Customer(String name, String email) {
        this.customerName = name;
        this.customerEmail = email;

        // Thiết lập tiêu đề cho giao diện
        setTitle("Customer View - Phone Shopping");
        setSize(1600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tạo JLabel "Hi, [name]" với phong cách đẹp hơn
        JLabel welcomeLabel = new JLabel("Hi, " + name);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(25, 118, 210));

        // Thiết lập màu nền
        getContentPane().setBackground(new Color(242, 242, 242));

        // Load dữ liệu từ CSV
        phoneList = CsvFileHandler.readPhonesFromCSV("phones.csv");

        // Tạo bảng dữ liệu
        String[] columnNames = {"Phone ID", "Brand", "Model", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        phoneTable = new JTable(tableModel);
        phoneTable.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneTable.setRowHeight(25);
        phoneTable.setGridColor(Color.LIGHT_GRAY);
        phoneTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        phoneTable.getTableHeader().setBackground(new Color(220, 220, 220));
        loadPhoneDataToTable(phoneList);

        JScrollPane scrollPane = new JScrollPane(phoneTable);

        // Tạo các nút và ô tìm kiếm với thiết kế đẹp hơn
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        addToCartButton.setBackground(new Color(76, 175, 80));
        addToCartButton.setForeground(Color.WHITE);

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(33, 150, 243));
        searchButton.setForeground(Color.WHITE);

        backButton = new JButton("Exit to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(189, 189, 189));
        backButton.setForeground(Color.BLACK);

        checkCartButton = new JButton("Check Cart"); // Nút kiểm tra giỏ hàng
        checkCartButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkCartButton.setBackground(new Color(255, 152, 0));
        checkCartButton.setForeground(Color.WHITE);

        // Tạo các nút "Refresh" và "Exit to Login"
        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);

        searchField = new JTextField(15);

        // Lấy danh sách thương hiệu duy nhất từ phoneList
        Set<String> uniqueBrands = new HashSet<>();
        for (Phones phone : phoneList) {
            uniqueBrands.add(phone.getBrand());
        }

        // Tạo combo box lọc và thêm các thương hiệu duy nhất
        filterComboBox = new JComboBox<>();
        filterComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterComboBox.addItem("All"); // Tùy chọn mặc định là "All"
        for (String brand : uniqueBrands) {
            filterComboBox.addItem("Brand: " + brand);
        }

        // Tạo panel chứa các nút và ô tìm kiếm
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(242, 242, 242));
        controlPanel.add(new JLabel("Search by Phone ID:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(filterComboBox);
        controlPanel.add(addToCartButton);
        controlPanel.add(checkCartButton); // Thêm nút Check Cart
        controlPanel.add(backButton); // Thêm nút Back
        controlPanel.add(refreshButton); // Thêm nút Refresh

        // Thêm các thành phần vào JFrame
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện cho nút Tìm kiếm
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                display(customerName, customerEmail);
            }
        });
        
        // Xử lý sự kiện cho nút Tìm kiếm
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchId = searchField.getText();
                ArrayList<Phones> filteredPhones = searchPhoneById(searchId);
                loadPhoneDataToTable(filteredPhones);
            }
        });

        // Xử lý sự kiện cho nút Add to Cart
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = phoneTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a phone to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Lấy thông tin sản phẩm được chọn
                String phoneId = (String) tableModel.getValueAt(selectedRow, 0);
                String phoneBrand = (String) tableModel.getValueAt(selectedRow, 1);
                String phoneModel = (String) tableModel.getValueAt(selectedRow, 2);
                String phonePrice = (String) tableModel.getValueAt(selectedRow, 3);

                // Lấy số lượng hàng tồn kho
                int stockQuantity;
                try {
                    stockQuantity = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Stock quantity is not a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Nhập số lượng sản phẩm
                String quantityStr = JOptionPane.showInputDialog("Enter quantity:");
                if (quantityStr == null || quantityStr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Quantity is required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity > stockQuantity) {
                        JOptionPane.showMessageDialog(null, "Not enough stock available.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String quantityString = Integer.toString(quantity);

                // Nhập mã giảm giá (nếu có)
                String discountCode = JOptionPane.showInputDialog("Enter discount code (optional):");
                if (discountCode == null || discountCode.trim().isEmpty()) {
                    discountCode = "No discount";
                }

                // Lấy tình trạng đơn hàng hiện tại
                String status = "Pending";

                // Lấy thời gian hiện tại
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                Phones y = new Phones(phoneId, phoneBrand, phoneModel, phonePrice, quantityString);
                Order x = new Order(customerEmail, y, timeStamp, discountCode, status);

                // Ghi thông tin vào file CSV
                CsvFileHandler.writeOrderToCSV(x);

                // Cập nhật số lượng hàng tồn kho
                updateStockQuantity(phoneId, stockQuantity - quantity);

                JOptionPane.showMessageDialog(null, "You have successfully added the product to the cart.", "Cart", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Xử lý sự kiện cho nút Back
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm.display();
                dispose(); // Đóng giao diện hiện tại
            }
        });

        // Xử lý sự kiện lọc theo combo box
        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            ArrayList<Phones> filteredPhones = filterPhones(selectedFilter);
            loadPhoneDataToTable(filteredPhones);
        });

        // Đặt giao diện ra giữa màn hình
        setLocationRelativeTo(null);

        // Xử lý sự kiện cho nút Check Cart
        checkCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCart();
            }
        });

        // Đặt giao diện ra giữa màn hình
        setLocationRelativeTo(null);
    }

    private void updateStockQuantity(String phoneId, int newQuantity) {
        String newQuantityStr = String.valueOf(newQuantity);
        for (Phones phone : phoneList) {
            if (phone.getPhoneId().equalsIgnoreCase(phoneId)) {
                phone.setStockQuantity(newQuantityStr);
                break;
            }
        }
        loadPhoneDataToTable(phoneList);
        CsvFileHandler.writePhonesToCSV(phoneList, "Phones.csv");
    }

    // Hàm tìm kiếm điện thoại theo mã
    private ArrayList<Phones> searchPhoneById(String phoneId) {
        if (phoneId.equals("")) {
            return phoneList;
        }
        ArrayList<Phones> result = new ArrayList<>();
        for (Phones phone : phoneList) {
            if (phone.getPhoneId().toLowerCase().contains(phoneId.toLowerCase())) {
                result.add(phone);
            }
        }
        return result;
    }

    // Hàm lọc điện thoại theo tiêu chí lọc đã chọn
    private ArrayList<Phones> filterPhones(String filter) {
        if (filter.equals("All")) {
            return phoneList;
        }
        ArrayList<Phones> filteredPhones = new ArrayList<>();
        for (Phones phone : phoneList) {
            if (filter.startsWith("Brand: ")) {
                String brand = filter.substring(7);
                if (phone.getBrand().equalsIgnoreCase(brand)) {
                    filteredPhones.add(phone);
                }
            }
        }
        return filteredPhones;
    }

    // Phương thức hiển thị giỏ hàng với checkbox để xác nhận các đơn hàng muốn mua
    private void showCart() {
        ArrayList<Order> allOrders = CsvFileHandler.readOrdersFromCSV();
        ArrayList<Order> customerOrders = new ArrayList<>();

        System.out.println("Day la: " + customerEmail);

        // Lọc các đơn hàng của khách hàng hiện tại
        for (Order order : allOrders) {
            if (order.getEmail().equals(customerEmail.replace("@gmail.com", ""))) {
                customerOrders.add(order);
            }
        }

        // Tạo cửa sổ giỏ hàng
        JFrame cartFrame = new JFrame("Your Cart");
        cartFrame.setSize(600, 400);
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

// Tạo bảng không có cột Select
        String[] columnNames = {"Phone ID", "Brand", "Model", "Price", "Quantity", "Status"};
        DefaultTableModel cartTableModel = new DefaultTableModel(columnNames, 0);

        JTable cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(25);

// Load dữ liệu đơn hàng vào bảng
        for (Order order : customerOrders) {
            Object[] row = {
                order.getPhones().getPhoneId(),
                order.getPhones().getBrand(),
                order.getPhones().getModel(),
                order.getPhones().getPrice(),
                order.getPhones().getStockQuantity(),
                order.getStatus()
            };
            cartTableModel.addRow(row);
        }

        JScrollPane cartScrollPane = new JScrollPane(cartTable);

// Nút xóa sản phẩm khỏi giỏ hàng
        JButton deleteSelectedButton = new JButton("Delete Selected Item");
        deleteSelectedButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteSelectedButton.setBackground(new Color(255, 0, 0));
        deleteSelectedButton.setForeground(Color.WHITE);

        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();

                // Kiểm tra nếu có một dòng được chọn
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(cartFrame, "Please select an item to delete.");
                    return;
                }

                // Nếu có một sản phẩm được chọn, tiến hành xóa
                Order orderToDelete = customerOrders.get(selectedRow);
                allOrders.remove(orderToDelete); // Xóa sản phẩm khỏi allOrders

                // Lấy thông tin sản phẩm từ đơn hàng vừa xóa
                Phones phoneInOrder = orderToDelete.getPhones();
                String phoneId = phoneInOrder.getPhoneId();
                int quantityToAddBack = Integer.parseInt(phoneInOrder.getStockQuantity());

                // Tìm sản phẩm trong danh sách `phoneList` và cập nhật lại số lượng tồn kho
                for (Phones phone : phoneList) {
                    if (phone.getPhoneId().equalsIgnoreCase(phoneId)) {
                        int currentStock = Integer.parseInt(phone.getStockQuantity());
                        phone.setStockQuantity(String.valueOf(currentStock + quantityToAddBack));
                        break;
                    }
                }

                // Ghi đè danh sách đơn hàng còn lại vào file "unconfirmOrders.csv"
                CsvFileHandler.writeOrdersToUnconfirmOrders(allOrders);

                // Cập nhật file "Phones.csv" với số lượng tồn kho mới
                CsvFileHandler.writePhonesToCSV(phoneList, "Phones.csv");

                JOptionPane.showMessageDialog(cartFrame, "Selected item has been deleted from your cart and stock quantity has been updated.");
                cartFrame.dispose(); // Đóng cửa sổ giỏ hàng sau khi xóa
            }
        });

// Thêm các thành phần vào cửa sổ giỏ hàng
        cartFrame.setLayout(new BorderLayout());
        cartFrame.add(cartScrollPane, BorderLayout.CENTER);
        cartFrame.add(deleteSelectedButton, BorderLayout.SOUTH);

// Hiển thị cửa sổ giỏ hàng
        cartFrame.setLocationRelativeTo(this);
        cartFrame.setVisible(true);
    }

    // Hàm load dữ liệu từ ArrayList<Phones> lên bảng
    private void loadPhoneDataToTable(ArrayList<Phones> phones) {
        tableModel.setRowCount(0); // Xóa các hàng cũ
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

    public static void display(String name, String email) {
        SwingUtilities.invokeLater(() -> {
            View_Customer view = new View_Customer(name, email);
            view.setVisible(true);
        });
    }
}
