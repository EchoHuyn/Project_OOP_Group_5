package loginAnhRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class View_Seller extends JFrame {

    private JButton btnCustomerList;
    private JButton btnProductList;
    private JButton btnUnconfirmedOrders;
    private JButton btnBackToLogin;
    private JButton btnCreateDiscount; // Nút tạo mã giảm giá

    public View_Seller() {
        setTitle("Giao diện người bán");
        setSize(500, 450);
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
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBackground(new Color(45, 52, 54));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 70, 10, 70));

        // Nút Danh sách người mua hàng
        btnCustomerList = new JButton("Danh sách người mua hàng");
        styleButton(btnCustomerList);
        buttonPanel.add(btnCustomerList);

        btnCustomerList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_Seller_Customer.display();
                dispose();
            }
        });

        // Nút Danh sách sản phẩm
        btnProductList = new JButton("Danh sách sản phẩm");
        styleButton(btnProductList);
        buttonPanel.add(btnProductList);
        btnProductList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_Seller_Phones.display();
                dispose();
            }
        });

        // Nút Các đơn hàng chưa xác nhận
        btnUnconfirmedOrders = new JButton("Các đơn hàng chưa xác nhận");
        styleButton(btnUnconfirmedOrders);
        buttonPanel.add(btnUnconfirmedOrders);

        btnUnconfirmedOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_Seller_Order.display();
                dispose();
            }
        });

        // Nút Tạo mã giảm giá
        btnCreateDiscount = new JButton("Tạo mã giảm giá");
        styleButton(btnCreateDiscount);
        buttonPanel.add(btnCreateDiscount);

        btnCreateDiscount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDiscountCode();
            }
        });

        // Nút Quay lại đăng nhập
        btnBackToLogin = new JButton("Quay lại");
        btnBackToLogin.setForeground(Color.RED);
        styleButton(btnBackToLogin);
        buttonPanel.add(btnBackToLogin);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

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

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(0, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    }

    private void createDiscountCode() {
        JTextField discountCodeField = new JTextField();
        JTextField discountAmountField = new JTextField();

        Object[] message = {
            "Mã giảm giá:", discountCodeField,
            "Mức giảm (VND):", discountAmountField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Tạo mã giảm giá", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String discountCode = discountCodeField.getText();
            String discountAmount = discountAmountField.getText();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("DiscountCode.csv", true))) {
                writer.write(discountCode + "," + discountAmount);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Mã giảm giá đã được thêm thành công!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu mã giảm giá", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            new View_Seller().setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        display();
    }
}
