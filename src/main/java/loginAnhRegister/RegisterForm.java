package loginAnhRegister;

import extensions.SendEmail;
import extensions.Check_OTP;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RegisterForm extends JFrame {

    private JTextField txtName, txtEmail, txtAddress, txtUsername;
    private JPasswordField txtPassword, txtPasswordConfirm;
    private JComboBox<Integer> comboDay, comboMonth, comboYear;
    private JComboBox<String> comboGender;
    private JButton btnRegister;
    private String OTP;
    private String Email;

    public RegisterForm() {
        setTitle("Register Form");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(11, 2, 10, 10));

        // Tên
        add(new JLabel("Tên:"));
        txtName = new JTextField();
        add(txtName);

        // Ngày tháng năm sinh
        add(new JLabel("Ngày sinh:"));

        JPanel birthPanel = new JPanel(new GridLayout(1, 3));
        comboDay = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            comboDay.addItem(i);
        }

        comboMonth = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            comboMonth.addItem(i);
        }

        comboYear = new JComboBox<>();
        for (int i = 2024; i >= 1900; i--) {
            comboYear.addItem(i);
        }

        birthPanel.add(comboDay);
        birthPanel.add(comboMonth);
        birthPanel.add(comboYear);
        add(birthPanel);

        // Giới tính
        add(new JLabel("Giới tính:"));
        comboGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        add(comboGender);

        // Email
        add(new JLabel("Gmail:"));
        txtEmail = new JTextField();
        add(txtEmail);

        // Địa chỉ nhận hàng
        add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        add(txtAddress);

        // Tài khoản đăng nhập
        add(new JLabel("Tài khoản:"));
        txtUsername = new JTextField();
        add(txtUsername);

        // Mật khẩu
        add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        // Nhập lại mật khẩu
        add(new JLabel("Nhập lại mật khẩu:"));
        txtPasswordConfirm = new JPasswordField();
        add(txtPasswordConfirm);

        // Nút đăng kí
        btnRegister = new JButton("Đăng kí");
        add(btnRegister);

        // Add "Back to Login" button
        JButton btnBackToLogin = new JButton("Back to Login");
        add(btnBackToLogin);

        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        RegisterForm.this,
                        "Bạn có chắc chắn muốn quay lại khung đăng nhập?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    LoginForm.display(); // Assumes LoginForm has a display method to show the login form
                    dispose();
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    Email = txtEmail.getText();
                    OTP = Check_OTP.generateOTP();
                    SendEmail.sendEmail_OTP(OTP, Email); // Gửi OTP qua email
                    String inputOtp = JOptionPane.showInputDialog("Nhập mã OTP đã được gửi:");

                    // Kiểm tra mã OTP nhập vào
                    if (inputOtp != null && inputOtp.equals(OTP)) {
                        saveAccountToFile(
                                txtName.getText(),
                                getSelectedDate(),
                                comboGender.getSelectedItem().toString(),
                                txtAddress.getText(),
                                txtUsername.getText(),
                                new String(txtPassword.getPassword()),
                                txtEmail.getText()
                        );
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Mã OTP không chính xác, vui lòng thử lại!");
                    }
                }
            }
        });
    }

    // Hàm lấy ngày sinh từ ComboBox
    private String getSelectedDate() {
        int day = (int) comboDay.getSelectedItem();
        int month = (int) comboMonth.getSelectedItem();
        int year = (int) comboYear.getSelectedItem();
        return day + "/" + month + "/" + year;
    }

    // Hàm kiểm tra form
    private boolean validateForm() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtPasswordConfirm.getPassword());

        // Kiểm tra email đuôi @gmail.com
        if (!email.endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(this, "Email phải có đuôi @gmail.com");
            return false;
        }

        // Kiểm tra mật khẩu có khớp nhau không
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp nhau, vui lòng nhập lại!");
            return false;
        }

        // Kiểm tra xem email đã tồn tại trong file CSV chưa
        if (isEmailExist(email)) {
            JOptionPane.showMessageDialog(this, "Email này đã được sử dụng. Vui lòng chọn email khác.");
            return false;
        }

        return true;
    }

    // Hàm kiểm tra xem email có tồn tại trong file account.csv không
    private boolean isEmailExist(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("accountCustomers.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] accountDetails = line.split(",");
                if (accountDetails.length > 2 && accountDetails[2].equals(email)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Hàm lưu tài khoản vào file accountCustomers.csv
    private void saveAccountToFile(String name, String birthDate, String gender, String address, String username, String password, String email) {
        try (FileWriter writer = new FileWriter("accountCustomers.csv", true)) { // Mở file ở chế độ ghi tiếp (append mode)
            writer.append(name)
                    .append(",")
                    .append(gender)
                    .append(",")
                    .append(birthDate)
                    .append(",")
                    .append(address)
                    .append(",")
                    .append(username)
                    .append(",")
                    .append(password)
                    .append(",")
                    .append(email)
                    .append("\n");
            writer.flush();
            JOptionPane.showMessageDialog(this, "Tài khoản đã được lưu vào file accountCustomers.csv");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi lưu tài khoản vào file");
        }
    }

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            new RegisterForm().setVisible(true);
        });
    }
}
