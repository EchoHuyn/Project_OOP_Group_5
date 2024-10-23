package loginAnhRegister;

import Model.Phones;
import extensions.CsvFileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View_Customer extends JFrame {

    private JTable phoneTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addToCartButton;
    private JButton searchButton;
    private JComboBox<String> filterComboBox;

    private ArrayList<Phones> phoneList;

    public View_Customer() {
        // Thiết lập tiêu đề cho giao diện
        setTitle("Customer View - Phone Shopping");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        searchField = new JTextField(15);

        // Tạo combo box để lọc theo tiêu chí
        String[] filterOptions = {"All", "Brand: Apple", "Brand: Samsung", "Price > 500"};
        filterComboBox = new JComboBox<>(filterOptions);

        // Tạo panel chứa các nút và ô tìm kiếm
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Search by Phone ID:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(filterComboBox);
        controlPanel.add(addToCartButton);

        // Thêm các thành phần vào JFrame
        setLayout(new BorderLayout());
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

        // Xử lý sự kiện cho nút Thêm giỏ hàng
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You have successfully added the product to the cart.", "Cart", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Đặt giao diện ra giữa màn hình
        setLocationRelativeTo(null);
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
        if(phoneId.equals("")){
            return phoneList;
        }
        ArrayList<Phones> result = new ArrayList<>();
        for (Phones phone : phoneList) {
            if (phone.getPhoneId().equalsIgnoreCase(phoneId)) {
                result.add(phone);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View_Customer view = new View_Customer();
                view.setVisible(true);
            }
        });
    }
}
