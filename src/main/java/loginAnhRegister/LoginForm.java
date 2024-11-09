package loginAnhRegister;

import Seller.View_Seller;
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
        setTitle("Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel and set background color
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(60, 63, 65));

        // Title label
        JLabel lblTitle = new JLabel("Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(60, 63, 65));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Select role:");

        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);
        roleLabel.setForeground(Color.WHITE);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        // ComboBox for role selection
        String[] roles = {"customer", "admin"};
        roleComboBox = new JComboBox<>(roles);

        // Add components to inputPanel
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(roleLabel);
        inputPanel.add(roleComboBox);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65));

        styleButton(loginButton);
        styleButton(registerButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event handling for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = roleComboBox.getSelectedItem().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter username and password!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if (checkLogin(username, password, role)) {
                        JOptionPane.showMessageDialog(null, "Login successful as role: " + role);
                        if (role.equals("customer")) {
                            for (Customer x : Customers) {
                                if (x.getUsername().equals(username) && x.getPassword().equals(password)) {
                                    View_Customer.display(x.getName(), x.getEmail());
                                    dispose();
                                }
                            }
                        }
                        if (role.equals("admin")) {
                            View_Seller.display();
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Event handling for register button
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to register?",
                        "Confirm registration",
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

    // Method for button styling
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

                String inputOtp = JOptionPane.showInputDialog(null, "Please enter the OTP:");

                if (inputOtp != null && inputOtp.equals(OTP)) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect OTP!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return false;
    }

    public static void display() {
        new LoginForm();
    }
}
