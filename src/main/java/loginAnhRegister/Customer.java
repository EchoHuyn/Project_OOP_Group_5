package loginAnhRegister;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableRowSorter;

public class Customer extends JFrame {

    private JTable phoneTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterBox;

    // Constructor
    public Customer() {
        setTitle("Phone Store");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel chính
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Bảng hiển thị điện thoại
        String[] columns = {"Phone ID", "Brand", "Model", "Price", "Stock Quantity"};
        tableModel = new DefaultTableModel(columns, 0);
        phoneTable = new JTable(tableModel);
        loadDataFromCSV("Phones.csv"); // Load dữ liệu từ file CSV

        JScrollPane scrollPane = new JScrollPane(phoneTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel tìm kiếm và lọc
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchProduct());
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Nút lọc sản phẩm theo tiêu chí
        String[] filters = {"All", "Samsung", "Apple", "Xiaomi", "OnePlus"};
        filterBox = new JComboBox<>(filters);
        filterBox.addActionListener(e -> filterProducts());
        searchPanel.add(new JLabel("Filter by brand: "));
        searchPanel.add(filterBox);

        // Nút mua hàng
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> buyProduct());
        searchPanel.add(buyButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        
        add(panel);
    }

    // Hàm load dữ liệu từ CSV
    private void loadDataFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals("phone_id")) {
                    tableModel.addRow(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm tìm kiếm sản phẩm theo model hoặc brand
    private void searchProduct() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a product to search.");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) phoneTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        phoneTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    // Hàm lọc sản phẩm theo brand
    private void filterProducts() {
        String selectedBrand = (String) filterBox.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) phoneTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        phoneTable.setRowSorter(sorter);
        if ("All".equals(selectedBrand)) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter(selectedBrand));
        }
    }

    // Hàm mua sản phẩm
    private void buyProduct() {
        int selectedRow = phoneTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to buy.");
            return;
        }
        String model = (String) phoneTable.getValueAt(selectedRow, 2);
        String stockStr = (String) phoneTable.getValueAt(selectedRow, 4);
        int stock = Integer.parseInt(stockStr);

        if (stock > 0) {
            stock--;
            phoneTable.setValueAt(String.valueOf(stock), selectedRow, 4);
            JOptionPane.showMessageDialog(this, "You have successfully bought: " + model);
        } else {
            JOptionPane.showMessageDialog(this, "Out of stock!");
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Customer app = new Customer();
            app.setVisible(true);
        });
    }
}
