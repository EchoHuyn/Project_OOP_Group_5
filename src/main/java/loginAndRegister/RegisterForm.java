package loginAndRegister;

import extensions.SendEmail;
import extensions.Check_OTP;
import extensions.Exception_Handling;
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
        setTitle("Register");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background color for the entire JFrame
        getContentPane().setBackground(new Color(60, 63, 65)); // dark gray color

        // Set the main layout
        setLayout(new BorderLayout());

        // Centered title
        JLabel lblTitle = new JLabel("Register", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Set background color for lblTitle to match the dark gray background
        lblTitle.setBackground(new Color(60, 63, 65));
        lblTitle.setOpaque(true); // Ensure the background color of lblTitle is visible
        add(lblTitle, BorderLayout.NORTH);

        // Panel containing components
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(60, 63, 65)); // consistent background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Input fields
        addField("Name:", txtName = new JTextField(15), mainPanel, gbc);
        addField("Date of Birth:", createBirthPanel(), mainPanel, gbc);
        addField("Gender:", comboGender = new JComboBox<>(new String[]{"Female", "Male", "Other"}), mainPanel, gbc);
        addField("Email:", txtEmail = new JTextField(), mainPanel, gbc);
        addField("Address:", txtAddress = new JTextField(), mainPanel, gbc);
        addField("Username:", txtUsername = new JTextField(), mainPanel, gbc);
        addField("Password:", txtPassword = new JPasswordField(), mainPanel, gbc);
        addField("Confirm Password:", txtPasswordConfirm = new JPasswordField(), mainPanel, gbc);

        // Create and add buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(60, 63, 65)); // consistent background color
        btnRegister = new JButton("Confirm Registration");
        styleButton(btnRegister);

        JButton btnBackToLogin = new JButton("Back to Login");
        styleButton(btnBackToLogin);

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBackToLogin);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Add mainPanel to the center of JFrame
        add(mainPanel, BorderLayout.CENTER);

        // Button event handling
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    Email = txtEmail.getText();
                    OTP = Check_OTP.generateOTP();
                    SendEmail.sendEmail_OTP(OTP, Email); // Send OTP via email
                    String inputOtp = JOptionPane.showInputDialog("Enter the OTP sent to your email:");

                    if (inputOtp != null && inputOtp.equals(OTP)) {
                        saveAccountToFile(
                                txtName.getText(),
                                getFormattedDate(), // Use formatted date
                                comboGender.getSelectedItem().toString(),
                                txtAddress.getText(),
                                txtUsername.getText(),
                                new String(txtPassword.getPassword()),
                                txtEmail.getText()
                        );
                        JOptionPane.showMessageDialog(RegisterForm.this, "Registration successful!");
                        LoginForm.display();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect OTP, please try again!");
                    }
                }
            }
        });

        btnBackToLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        RegisterForm.this,
                        "Are you sure you want to go back to login?",
                        "Confirmation",
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

    // Form validation method
    private boolean validateForm() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtPasswordConfirm.getPassword());

        // Validate date of birth
        int day = (int) comboDay.getSelectedItem();
        int month = (int) comboMonth.getSelectedItem();
        int year = (int) comboYear.getSelectedItem();

        if (!Exception_Handling.isValidDate(day, month, year)) {
            JOptionPane.showMessageDialog(this, "Invalid date of birth, please enter again!");
            return false;
        }

        // Check if email ends with @gmail.com
        if (!email.endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(this, "Email must end with @gmail.com");
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match, please enter again!");
            return false;
        }

        // Check if email already exists in CSV file
        if (Exception_Handling.isEmailExist(email)) {
            JOptionPane.showMessageDialog(this, "This email is already in use. Please choose another email.");
            return false;
        }

        // Standardize name and address
        txtName.setText(Exception_Handling.formatName(txtName.getText()));
        txtAddress.setText(Exception_Handling.formatName(txtAddress.getText()));

        return true;
    }   

    // Method to save account to accountCustomers.csv
    private void saveAccountToFile(String name, String birthDate, String gender, String address, String username, String password, String email) {
        try (FileWriter writer = new FileWriter("accountCustomers.csv", true)) { // Open file in append mode
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
            JOptionPane.showMessageDialog(this, "Account has been saved to accountCustomers.csv");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while saving the account");
        }
    }

    // Modified getSelectedDate method to return date in dd/mm/yyyy format
    private String getFormattedDate() {
        int day = (int) comboDay.getSelectedItem();
        int month = (int) comboMonth.getSelectedItem();
        int year = (int) comboYear.getSelectedItem();
        return String.format("%02d/%02d/%04d", day, month, year); // Ensure two-digit day and month with zero padding
    }

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            new RegisterForm().setVisible(true);
        });
    }
}
