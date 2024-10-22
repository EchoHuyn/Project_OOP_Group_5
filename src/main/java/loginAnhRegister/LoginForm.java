package loginAnhRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginForm() {
        // Tạo giao diện
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        // Tạo các thành phần
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Đăng nhập");
        registerButton = new JButton("Đăng kí");

        // Thêm các thành phần vào khung
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        // Đặt khung ở giữa màn hình
        setLocationRelativeTo(null);

        // Xử lý sự kiện khi bấm nút Đăng nhập
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Kiểm tra nếu ô tên đăng nhập hoặc mật khẩu trống
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập tên đăng nhập và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Nếu cả hai đều đã được nhập, kiểm tra thông tin đăng nhập
                    if (checkLogin(username, password)) {
                        JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Tên đăng nhập hoặc mật khẩu sai!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Xử lý sự kiện khi bấm nút Đăng kí
        // Xử lý sự kiện khi bấm nút Đăng kí
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Bạn có muốn đăng kí không?",
                        "Xác nhận đăng kí",
                        JOptionPane.OK_CANCEL_OPTION
                );

                // Kiểm tra người dùng bấm nút nào
                if (option == JOptionPane.OK_OPTION) {
                    // Người dùng bấm OK, thực hiện các thao tác thêm tại đây
                    //JOptionPane.showMessageDialog(null, "Bạn đã chọn đăng kí!");
                    // Ở đây, bạn có thể thêm mã để mở form đăng kí mới hoặc các hành động khác
                    RegisterForm.display();
                    dispose();
                } else {
                    // Người dùng bấm Không (Cancel)
                    //JOptionPane.showMessageDialog(null, "Bạn đã chọn không đăng kí.");
                }
            }
        });

        setVisible(true);
    }

    // Phương thức kiểm tra tên đăng nhập và mật khẩu từ file CSV
    private boolean checkLogin(String username, String password) {
        String line;
        String csvFile = "accountCustomers.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(username) && values[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
