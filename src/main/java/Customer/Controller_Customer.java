package Customer;

import Model.Order;
import Model.Phones;
import extensions.CsvFileHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Controller_Customer {   
    // Hàm load dữ liệu từ ArrayList<Phones> lên bảng
    public static void loadPhoneDataToTable(DefaultTableModel tableModel, ArrayList<Phones> phones) {
        tableModel.setRowCount(0); // Xóa các hàng cũ
        for (Phones phone : phones) {
            Object[] rowData = {
                phone.getPhoneId(),
                phone.getBrand(),
                phone.getModel(),
                phone.getPrice(),
                phone.getStockQuantity()
            };
            tableModel.addRow(rowData);
        }
    }
    
    // Hàm lọc điện thoại theo tiêu chí lọc đã chọn
    public static ArrayList<Phones> filterPhones(String filter, ArrayList<Phones> phoneList) {
        if (filter.equals("All Brand")) {
            return phoneList;
        }
        ArrayList<Phones> filteredPhones = new ArrayList<>();
        for (Phones phone : phoneList) {
            if (filter.startsWith("Brand: ")) {
                String brand = filter.substring(7);
                if (phone.getBrand().equalsIgnoreCase(brand)) {
                    filteredPhones.add(phone);
                }
            }
        }
        return filteredPhones;
    }
    
    public static void updateStockQuantity(String phoneId, int newQuantity, ArrayList<Phones> phoneList, DefaultTableModel tableModel) {
        String newQuantityStr = String.valueOf(newQuantity);
        for (Phones phone : phoneList) {
            if (phone.getPhoneId().equalsIgnoreCase(phoneId)) {
                phone.setStockQuantity(newQuantityStr);
                break;
            }
        }
        Controller_Customer.loadPhoneDataToTable(tableModel, phoneList);
        CsvFileHandler.writePhonesToCSV(phoneList, "Phones.csv");
    }
    
    // Hàm tìm kiếm điện thoại theo mã
    public static ArrayList<Phones> searchPhoneById(String phoneId, ArrayList<Phones> phoneList) {
        if (phoneId.equals("")) {
            return phoneList;
        }
        ArrayList<Phones> result = new ArrayList<>();
        for (Phones phone : phoneList) {
            if (phone.getPhoneId().toLowerCase().contains(phoneId.toLowerCase())) {
                result.add(phone);
            }
        }
        return result;
    }
}
