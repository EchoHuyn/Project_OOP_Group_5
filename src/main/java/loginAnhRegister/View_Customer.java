package loginAnhRegister;

import Model.Phones;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class View_Customer extends JFrame {

    private JTable phoneTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addToCartButton;
    private JButton searchButton;
    private JButton backButton; // Thêm nút back
    private JComboBox<String> filterComboBox;

    private ArrayList<Phones> phoneList;
    private String customerName;
    private String customerEmail;

    public View_Customer(String name, String email) {
        this.customerName = name;
        this.customerEmail = email;

        // Thiết lập tiêu đề cho giao diện
        setTitle("Customer View - Phone Shopping");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tạo JLabel "Hi, [name]"
        JLabel welcomeLabel = new JLabel("Hi, " + name);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));  // Font nổi bật
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.BLUE);  // Màu chữ nổi bật

        // Load dữ liệu từ CSV
        phoneList = CsvFileHandler.readPhonesFromCSV("phones.csv");

        // Tạo bảng dữ liệu
        String[] columnNames = {"Phone ID", "Brand", "Model", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0);
        phoneTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(phoneTable);
        loadPhoneDataToTable(phoneList); // Load dữ liệu vào bảng

        // Tạo các nút và ô tìm kiếm
        addToCartButton = new JButton("Add to Cart");
        searchButton = new JButton("Search");
        backButton = new JButton("Back"); // Nút Back
        searchField = new JTextField(15);

        // Lấy danh sách thương hiệu duy nhất từ phoneList
        Set<String> uniqueBrands = new HashSet<>();
        for (Phones phone : phoneList) {
            uniqueBrands.add(phone.getBrand());
        }

        // Tạo combo box lọc và thêm các thương hiệu duy nhất
        filterComboBox = new JComboBox<>();
        filterComboBox.addItem("All"); // Tùy chọn mặc định là "All"
        for (String brand : uniqueBrands) {
            filterComboBox.addItem("Brand: " + brand);
        }      

        // Tạo panel chứa các nút và ô tìm kiếm
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Search by Phone ID:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(filterComboBox);
        controlPanel.add(addToCartButton);
        controlPanel.add(backButton); // Thêm nút Back

        // Thêm các thành phần vào JFrame
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.NORTH); // Thêm JLabel "Hi, [name]" lên đầu
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện cho nút Tìm kiếm
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchId = searchField.getText();
                ArrayList<Phones> filteredPhones = searchPhoneById(searchId);
                loadPhoneDataToTable(filteredPhones);
            }
        });

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

                // Lấy số lượng hàng tồn kho dưới dạng chuỗi và chuyển đổi thành int
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

                // Nhập mã giảm giá (nếu có)
                String discountCode = JOptionPane.showInputDialog("Enter discount code (optional):");

                // Ghi thông tin vào file CSV
                saveOrderToCSV(phoneId, phoneBrand, phoneModel, phonePrice, quantity, discountCode);

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
    }

    private void updateStockQuantity(String phoneId, int newQuantity) {
        String newQuantityStr = String.valueOf(newQuantity); // Chuyển đổi int sang String
        for (Phones phone : phoneList) {
            if (phone.getPhoneId().equalsIgnoreCase(phoneId)) {
                phone.setStockQuantity(newQuantityStr); // Cập nhật số lượng trong danh sách dưới dạng chuỗi
                break;
            }
        }
        loadPhoneDataToTable(phoneList); // Cập nhật lại dữ liệu trong bảng
        CsvFileHandler.writePhonesToCSV(phoneList, "Phones.csv"); 
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
            return phoneList; // Trả về toàn bộ danh sách nếu chọn "All"
        }
        ArrayList<Phones> filteredPhones = new ArrayList<>();
        for (Phones phone : phoneList) {
            if (filter.startsWith("Brand: ")) {
                String brand = filter.substring(7); // Lấy tên thương hiệu
                if (phone.getBrand().equalsIgnoreCase(brand)) {
                    filteredPhones.add(phone);
                }
            } 
        }
        return filteredPhones;
    }

    private void saveOrderToCSV(String phoneId, String brand, String model, String price, int quantity, String discountCode) {
        // Tạo tên file từ email khách hàng
        String email = customerEmail.replace("@gmail.com", "");
        String logFileName  = "unconfirmOrders.csv";
        
        try (FileWriter logWriter = new FileWriter(logFileName, true)) {
            // Lấy thời gian hiện tại
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Ghi dữ liệu vào file
            String data = email + "," +
              phoneId + "," +
              brand + "," +
              model + "," +
              price + "," +
              String.valueOf(quantity) + "," +
              timeStamp + ",";
            
            if (discountCode != null && !discountCode.trim().isEmpty()) {
                data += discountCode;
            } else {
                data += "No discount";
            }
            logWriter.append(data);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving order to CSV.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void display(String name, String email) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View_Customer view = new View_Customer(name, email);
                view.setVisible(true);
            }
        });
    }

}
