package Seller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import loginAnhRegister.LoginForm;

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
        btnProductList = new JButton("Product List");
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
        btnUnconfirmedOrders = new JButton("Unconfirmed Orders");
        styleButton(btnUnconfirmedOrders);
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
        styleButton(btnManageDiscount);
        buttonPanel.add(btnManageDiscount);

        btnManageDiscount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageDiscountCode();
            }
        });

        // Nút Quay lại đăng nhập
        btnBackToLogin = new JButton("Back to Login");
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

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(0, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    }

    private void manageDiscountCode() {
        String[] options = {"Create Discount Code", "Delete Discount Code"};
        int choice = JOptionPane.showOptionDialog(this, "Select discount code action", "Manage Discount Codes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            createDiscountCode();
        } else if (choice == 1) {
            deleteDiscountCode();
        }
    }

    private void createDiscountCode() {
        JTextField discountCodeField = new JTextField();
        JTextField discountAmountField = new JTextField();

        Object[] message = {
            "Discount Code:", discountCodeField,
            "Discount Amount (Dollar):", discountAmountField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create Discount Code", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String discountCode = discountCodeField.getText();
            String discountAmount = discountAmountField.getText();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("DiscountCode.csv", true))) {
                writer.write(discountCode + "," + discountAmount);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Discount code successfully added!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving discount code", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteDiscountCode() {
        ArrayList<String> discountCodes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("DiscountCode.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                discountCodes.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading discount codes", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] codesArray = discountCodes.toArray(new String[0]);
        String selectedCode = (String) JOptionPane.showInputDialog(this, "Select discount code to delete:",
                "Delete Discount Code", JOptionPane.PLAIN_MESSAGE, null, codesArray, codesArray[0]);

        if (selectedCode != null) {
            discountCodes.remove(selectedCode);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("DiscountCode.csv"))) {
                for (String code : discountCodes) {
                    writer.write(code);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Discount code successfully deleted!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error deleting discount code", "Error", JOptionPane.ERROR_MESSAGE);
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
