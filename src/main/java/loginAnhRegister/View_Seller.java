package loginAnhRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View_Seller extends JFrame {

    private JButton btnCustomerList;
    private JButton btnProductList;
    private JButton btnBackToLogin;

    public View_Seller() {
        setTitle("Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo Panel chính và đặt màu nền
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(60, 63, 65));

        // Tiêu đề
        JLabel lblTitle = new JLabel("Menu", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(60, 63, 65));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        // Nút Danh sách người mua hàng
        btnCustomerList = new JButton("Danh sách người mua hàng");
        styleButton(btnCustomerList);
        buttonPanel.add(btnCustomerList);

        // Nút Danh sách sản phẩm
        btnProductList = new JButton("Danh sách sản phẩm");
        styleButton(btnProductList);
        buttonPanel.add(btnProductList);

        // Nút Back quay lại khung đăng nhập
        btnBackToLogin = new JButton("Back");
        btnBackToLogin.setForeground(Color.RED);
        styleButton(btnBackToLogin);
        buttonPanel.add(btnBackToLogin);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Xử lý sự kiện nút Back
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

    // Phương thức định dạng chung cho các nút
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void display() {
        SwingUtilities.invokeLater(() -> {
            new View_Seller().setVisible(true);
        });
    }   
}
