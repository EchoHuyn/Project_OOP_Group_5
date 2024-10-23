package loginAnhRegister;

import Model.Customer;
import Model.Phones;
import Model.Seller;
import extensions.SendEmail;
import extensions.Check_OTP;
import extensions.CsvFileHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JComboBox<String> roleComboBox; // Thêm combo box để chọn vai trò

    private ArrayList<Customer> Customers;
    private ArrayList<Seller> admin;

    public LoginForm() {
        // Tạo giao diện
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        // Tạo các thành phần
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        JLabel roleLabel = new JLabel("Chọn vai trò:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Đăng nhập");
        registerButton = new JButton("Đăng kí");

        // ComboBox để chọn vai trò
        String[] roles = {"customer", "admin"};
        roleComboBox = new JComboBox<>(roles);

        // Thêm các thành phần vào khung
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);  // Thêm label cho comboBox
        add(roleComboBox);  // Thêm comboBox
        add(loginButton);
        add(registerButton);

        // Đặt khung ở giữa màn hình
        setLocationRelativeTo(null);

        // Xử lý sự kiện khi bấm nút Đăng nhập
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = roleComboBox.getSelectedItem().toString(); // Lấy vai trò được chọn

                // Kiểm tra nếu ô tên đăng nhập hoặc mật khẩu trống
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập tên đăng nhập và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {

                    // Nếu cả hai đều đã được nhập, kiểm tra thông tin đăng nhập                   
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

    // Phương thức kiểm tra tên đăng nhập và mật khẩu dựa trên vai trò (admin/customer)
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

                //Sinh ma OTP va gui ve gmail
                String emailCheck = admin.get(0).getEmail();
                String OTP = Check_OTP.generateOTP();
                SendEmail.sendEmail_OTP(OTP, emailCheck);

                //Kiem tra otp
                String inputOtp = JOptionPane.showInputDialog(null, "\nVui lòng nhập mã OTP:");

                // Kiểm tra OTP
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
