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

public class RegisterForm extends JFrame {

    private JTextField txtName, txtEmail, txtAddress, txtUsername;
    private JPasswordField txtPassword, txtPasswordConfirm;
    private JComboBox<Integer> comboDay, comboMonth, comboYear;
    private JComboBox<String> comboGender;
    private JButton btnRegister;
    private String OTP;
    private String Email;

    public RegisterForm() {
        setTitle("Đăng Kí");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

// Đặt màu nền cho toàn bộ JFrame
        getContentPane().setBackground(new Color(60, 63, 65)); // màu xám đậm

// Đặt bố cục chính
        setLayout(new BorderLayout());

// Tiêu đề căn giữa
        JLabel lblTitle = new JLabel("Đăng Kí", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

// Đặt màu nền cho lblTitle để phù hợp với màu nền xám đậm
        lblTitle.setBackground(new Color(60, 63, 65));
        lblTitle.setOpaque(true); // Đảm bảo màu nền của lblTitle hiển thị
        add(lblTitle, BorderLayout.NORTH);

// Panel chứa các thành phần
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(60, 63, 65)); // màu nền đồng bộ

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Các trường nhập liệu
        addField("Tên:", txtName = new JTextField(15), mainPanel, gbc);
        addField("Ngày sinh:", createBirthPanel(), mainPanel, gbc);
        addField("Giới tính:", comboGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"}), mainPanel, gbc);
        addField("Gmail:", txtEmail = new JTextField(), mainPanel, gbc);
        addField("Địa chỉ:", txtAddress = new JTextField(), mainPanel, gbc);
        addField("Tài khoản:", txtUsername = new JTextField(), mainPanel, gbc);
        addField("Mật khẩu:", txtPassword = new JPasswordField(), mainPanel, gbc);
        addField("Nhập lại mật khẩu:", txtPasswordConfirm = new JPasswordField(), mainPanel, gbc);

        // Tạo và thêm nút bấm
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(60, 63, 65)); // màu nền đồng bộ
        btnRegister = new JButton("Xác nhận đăng kí");
        styleButton(btnRegister);

        JButton btnBackToLogin = new JButton("Quay lại đăng nhập");
        styleButton(btnBackToLogin);

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBackToLogin);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Thêm mainPanel vào trung tâm của JFrame
        add(mainPanel, BorderLayout.CENTER);

        // Xử lý sự kiện nút
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    Email = txtEmail.getText();
                    OTP = Check_OTP.generateOTP();
                    SendEmail.sendEmail_OTP(OTP, Email); // Gửi OTP qua email
                    String inputOtp = JOptionPane.showInputDialog("Nhập mã OTP đã được gửi:");

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
                        JOptionPane.showMessageDialog(RegisterForm.this, "Đăng kí thành công!");
                        LoginForm.display();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Mã OTP không chính xác, vui lòng thử lại!");
                    }
                }
            }
        });

        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        RegisterForm.this,
                        "Bạn có chắc chắn muốn quay lại khung đăng nhập?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION
                );

                if (response == JOptionPane.YES_OPTION) {
                    LoginForm.display();
                    dispose();
                }
            }
        });
    }

    private void addField(String label, Component component, JPanel panel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(Color.WHITE);
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private JPanel createBirthPanel() {
        JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        birthPanel.setBackground(new Color(60, 63, 65));

        return birthPanel;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

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

        // Kiểm tra ngày sinh hợp lệ
        int day = (int) comboDay.getSelectedItem();
        int month = (int) comboMonth.getSelectedItem();
        int year = (int) comboYear.getSelectedItem();

        if (!isValidDate(day, month, year)) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ, vui lòng nhập lại!");
            return false;
        }

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

        // Chuẩn hóa họ và tên
        txtName.setText(formatName(txtName.getText()));
        txtAddress.setText(formatName(txtAddress.getText()));

        return true;
    }

    //Hàm chuẩn hóa tên
    private String formatName(String name) {
        name = name.trim().replaceAll("\\s+", " "); // Loại bỏ khoảng trắng thừa
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0))) // Viết hoa chữ cái đầu
                        .append(word.substring(1).toLowerCase()) // Viết thường các chữ cái còn lại
                        .append(" ");
            }
        }

        return formattedName.toString().trim();
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

    //Hàm kiểm tra ngày tháng năm
    private boolean isValidDate(int day, int month, int year) {
        // Số ngày tối đa cho từng tháng
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // Kiểm tra năm nhuận
        if (month == 2 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
            daysInMonth[1] = 29; // Tháng 2 có 29 ngày trong năm nhuận
        }

        return day > 0 && day <= daysInMonth[month - 1];
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
