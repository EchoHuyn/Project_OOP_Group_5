package loginAnhRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class View_Seller extends JFrame {

    private JButton btnCustomerList;
    private JButton btnProductList;
    private JButton btnUnconfirmedOrders; // Nút mới cho đơn hàng chưa xác nhận
    private JButton btnBackToLogin;

    public View_Seller() {
        setTitle("Giao diện người bán");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo Panel chính và đặt màu nền
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(45, 52, 54));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Quản lý đơn hàng", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(new Color(45, 52, 54));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 70, 10, 70));

        // Nút Danh sách người mua hàng
        btnCustomerList = new JButton("Danh sách người mua hàng");
        styleButton(btnCustomerList);
        buttonPanel.add(btnCustomerList);

        // Nút Danh sách sản phẩm
        btnProductList = new JButton("Danh sách sản phẩm");
        styleButton(btnProductList);
        buttonPanel.add(btnProductList);

        // Nút Các đơn hàng chưa xác nhận
        btnUnconfirmedOrders = new JButton("Các đơn hàng chưa xác nhận");
        styleButton(btnUnconfirmedOrders);
        buttonPanel.add(btnUnconfirmedOrders);

        // Nút Quay lại đăng nhập
        btnBackToLogin = new JButton("Quay lại");
        btnBackToLogin.setForeground(Color.RED);
        styleButton(btnBackToLogin);
        buttonPanel.add(btnBackToLogin);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Xử lý sự kiện nút Quay lại
        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        View_Seller.this,
                        "Bạn có chắc chắn muốn quay lại khung đăng nhập?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    LoginForm.display();
                    dispose();
                }
            }
        });
    }

    // Phương thức định dạng chung cho các nút
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(0, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    }

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            new View_Seller().setVisible(true);
        });
    }

    public static void main(String[] args) {
        display();
    }
    
    public void addPhoneQuantity(String phoneId, int quantity) {
        File inputFile = new File("phones.csv");
        File tempFile = new File("phones_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String header = reader.readLine(); // Đọc dòng đầu tiên (nhãn cột)
            writer.write(header + System.lineSeparator());

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data[0].equals(phoneId)) {
                    int currentStock = Integer.parseInt(data[4]);
                    data[4] = String.valueOf(currentStock + quantity);
                    currentLine = String.join(",", data);
                }
                writer.write(currentLine + System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the original file
        if (!inputFile.delete()) {
            System.out.println("Could not delete file");
            return;
        }

        // Rename the new file to the filename the original file had.
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename file");
        }
    }
    
    public void confirmOrder(String email, String phoneId, String brand, String model, double price, int quantity, String timeStamp, String discountCode) {
        // Tạo tên file từ email
        String fileName = email.replace("@gmail.com", "") + ".txt";
        File orderFile = new File("orderHistory/" + fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFile, true))) {
            // Ghi dữ liệu vào file
            writer.write(phoneId + "," + brand + "," + model + "," + price + "," + quantity + "," + timeStamp + "," + discountCode);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void removeOrder(String email, String phoneId, String brand, String model, double price, int quantity, String timeStamp, String discountCode) {
        File inputFile = new File("unconfirmOrder.csv");
        File tempFile = new File("unconfirmOrder_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data.length == 8 &&
                    data[0].equals(email) &&
                    data[1].equals(phoneId) &&
                    data[2].equals(brand) &&
                    data[3].equals(model) &&
                    Double.parseDouble(data[4]) == price &&
                    Integer.parseInt(data[5]) == quantity &&
                    data[6].equals(timeStamp) &&
                    data[7].equals(discountCode)) {
                    continue; // Skip this line
                }
                writer.write(currentLine + System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the original file
        if (!inputFile.delete()) {
            System.out.println("Could not delete file");
            return;
        }

        // Rename the new file to the filename the original file had.
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename file");
        }
    }
}