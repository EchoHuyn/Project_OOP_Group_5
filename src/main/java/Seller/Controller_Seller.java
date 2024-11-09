package Seller;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Controller_Seller {
    public static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(0, 153, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
    }
    
    public static void manageDiscountCode() {
        String[] options = {"Create Discount Code", "Delete Discount Code"};
        int choice = JOptionPane.showOptionDialog(null, "Select discount code action", "Manage Discount Codes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            createDiscountCode();
        } else if (choice == 1) {
            deleteDiscountCode();
        }
    }
    
    public static void createDiscountCode() {
        JTextField discountCodeField = new JTextField();
        JTextField discountAmountField = new JTextField();

        Object[] message = {
            "Discount Code:", discountCodeField,
            "Discount Amount (Dollar):", discountAmountField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Create Discount Code", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String discountCode = discountCodeField.getText().trim();
            String discountAmount = discountAmountField.getText().trim();

            // Check if either field is empty
            if (discountCode.isEmpty() || discountAmount.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Both Discount Code and Amount are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate if discount amount is a valid positive number
            try {
                double amount = Double.parseDouble(discountAmount);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Discount Amount must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Discount Amount must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Save the discount code if validation passes
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("DiscountCode.csv", true))) {
                writer.write(discountCode + "," + discountAmount);
                writer.newLine();
                JOptionPane.showMessageDialog(null, "Discount code successfully added!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving discount code", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void deleteDiscountCode() {
        ArrayList<String> discountCodes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("DiscountCode.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                discountCodes.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading discount codes", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] codesArray = discountCodes.toArray(new String[0]);
        String selectedCode = (String) JOptionPane.showInputDialog(null, "Select discount code to delete:",
                "Delete Discount Code", JOptionPane.PLAIN_MESSAGE, null, codesArray, codesArray[0]);

        if (selectedCode != null) {
            discountCodes.remove(selectedCode);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("DiscountCode.csv"))) {
                for (String code : discountCodes) {
                    writer.write(code);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "Discount code successfully deleted!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error deleting discount code", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
