package Seller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import loginAndRegister.LoginForm;

public class View_Seller extends JFrame {

    private JButton btnCustomerList;
    private JButton btnProductList;
    private JButton btnUnconfirmedOrders;
    private JButton btnBackToLogin;
    private JButton btnManageDiscount; // Nút quản lý mã giảm giá

    public View_Seller() {
        setTitle("Seller Dashboard");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo Panel chính và đặt màu nền
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(45, 52, 54));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Order Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBackground(new Color(45, 52, 54));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 70, 10, 70));

        // Nút Danh sách người mua hàng
        btnCustomerList = new JButton("Customer List");
        Controller_Seller.styleButton(btnCustomerList);
        buttonPanel.add(btnCustomerList);

        btnCustomerList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_Seller_Customer.display();
                dispose();
            }
        });

        // Nút Danh sách sản phẩm
        btnProductList = new JButton("Product List");
        Controller_Seller.styleButton(btnProductList);
        buttonPanel.add(btnProductList);
        btnProductList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_Seller_Phones.display();
                dispose();
            }
        });

        // Nút Các đơn hàng chưa xác nhận
        btnUnconfirmedOrders = new JButton("Unconfirmed Orders");
        Controller_Seller.styleButton(btnUnconfirmedOrders);
        buttonPanel.add(btnUnconfirmedOrders);

        btnUnconfirmedOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                View_Seller_Order.display();
                dispose();
            }
        });

        // Nút Quản lý mã giảm giá
        btnManageDiscount = new JButton("Manage Discount Codes");
        Controller_Seller.styleButton(btnManageDiscount);
        buttonPanel.add(btnManageDiscount);

        btnManageDiscount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller_Seller.manageDiscountCode();
            }
        });

        // Nút Quay lại đăng nhập
        btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setForeground(Color.RED);
        Controller_Seller.styleButton(btnBackToLogin);
        buttonPanel.add(btnBackToLogin);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        View_Seller.this,
                        "Are you sure you want to return to the login screen?",
                        "Confirm",
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

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            new View_Seller().setVisible(true);
        });
    }

    public static void main(String[] args) {
        display();
    }
}
