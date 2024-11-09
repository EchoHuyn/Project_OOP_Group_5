package loginAnhRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class View_Seller extends JFrame {

    private JButton btnCustomerList;
    private JButton btnProductList;
    private JButton btnBackToLogin;

    public View_Seller() {
        setTitle("Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo Panel chính và đặt màu nền
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(60, 63, 65));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Menu", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        // Nút Danh sách người mua hàng
        btnCustomerList = new JButton("Danh sách người mua hàng");
        styleButton(btnCustomerList);
        buttonPanel.add(btnCustomerList);

        // Nút Danh sách sản phẩm
        btnProductList = new JButton("Danh sách sản phẩm");
        styleButton(btnProductList);
        buttonPanel.add(btnProductList);

        // Nút Back quay lại khung đăng nhập
        btnBackToLogin = new JButton("Back");
        btnBackToLogin.setForeground(Color.RED);
        styleButton(btnBackToLogin);
        buttonPanel.add(btnBackToLogin);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Xử lý sự kiện nút Back
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
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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