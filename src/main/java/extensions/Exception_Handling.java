package extensions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Exception_Handling {
    // Method to standardize name
    public static String formatName(String name) {
        name = name.trim().replaceAll("\\s+", " "); // Remove extra spaces
        String[] words = name.split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(Character.toUpperCase(word.charAt(0))) // Capitalize first letter
                        .append(word.substring(1).toLowerCase()) // Lowercase remaining letters
                        .append(" ");
            }
        }

        return formattedName.toString().trim();
    }

    // Method to check if email exists in accountCustomers.csv
    public static boolean isEmailExist(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("accountCustomers.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] accountDetails = line.split(",");
                if (accountDetails.length > 6 && accountDetails[6].equalsIgnoreCase(email)) { // Adjust index to 6 for email column
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Method to validate date
    public static boolean isValidDate(int day, int month, int year) {
        // Max days for each month
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // Check leap year
        if (month == 2 && ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))) {
            daysInMonth[1] = 29; // February has 29 days in leap years
        }

        return day > 0 && day <= daysInMonth[month - 1];
    }
}
