package loginAnhRegister;

import Model.Customer;
import Model.Seller;
import extensions.SendEmail;
import extensions.Check_OTP;
import extensions.CsvFileHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JComboBox<String> roleComboBox;

    private ArrayList<Customer> Customers;
    private ArrayList<Seller> admin;

    public LoginForm() {
        setTitle("Đăng Nhập");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo Panel chính và đặt màu nền
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(60, 63, 65));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Đăng Nhập", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel cho các trường nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(60, 63, 65));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        // Tạo các thành phần
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        JLabel roleLabel = new JLabel("Chọn vai trò:");

        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);
        roleLabel.setForeground(Color.WHITE);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Đăng nhập");
        registerButton = new JButton("Đăng kí");

        // ComboBox để chọn vai trò
        String[] roles = {"customer", "admin"};
        roleComboBox = new JComboBox<>(roles);

        // Thêm các thành phần vào inputPanel
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(roleLabel);
        inputPanel.add(roleComboBox);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel cho các nút hành động
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65));
        
        styleButton(loginButton);
        styleButton(registerButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Xử lý sự kiện khi bấm nút Đăng nhập
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = roleComboBox.getSelectedItem().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập tên đăng nhập và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (checkLogin(username, password, role)) {
                        JOptionPane.showMessageDialog(null, "Đăng nhập thành công với vai trò: " + role);
                        if (role.equals("customer")) {
                            for (Customer x : Customers) {
                                if (x.getUsername().equals(username) && x.getPassword().equals(password)) {
                                    View_Customer.display(x.getName());
                                    dispose();
                                }
                            }
                        }
                        if(role.equals("admin")){
                            View_Seller.display();
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu sai!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Xử lý sự kiện khi bấm nút Đăng kí
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có muốn đăng kí không?",
                        "Xác nhận đăng kí",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (option == JOptionPane.OK_OPTION) {
                    RegisterForm.display();
                    dispose();
                }
            }
        });

        setVisible(true);
    }

    // Phương thức định dạng chung cho các nút
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private boolean checkLogin(String username, String password, String role) {
        if (role.equals("customer")) {
            Customers = CsvFileHandler.readCustomersFromCSV("accountCustomers.csv");
            for (Customer x : Customers) {
                if (x.getUsername().equals(username) && x.getPassword().equals(password)) {
                    return true;
                }
            }
        } else {
            admin = CsvFileHandler.readSellersFromCSV("accountAdmin.csv");
            if (admin.get(0).getUsername().equals(username) && admin.get(0).getPassword().equals(password)) {
                String emailCheck = admin.get(0).getEmail();
                String OTP = Check_OTP.generateOTP();
                SendEmail.sendEmail_OTP(OTP, emailCheck);

                String inputOtp = JOptionPane.showInputDialog(null, "\nVui lòng nhập mã OTP:");

                if (inputOtp != null && inputOtp.equals(OTP)) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Mã OTP không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return false;
    }

    public static void display() {
        new LoginForm();
    }
}
