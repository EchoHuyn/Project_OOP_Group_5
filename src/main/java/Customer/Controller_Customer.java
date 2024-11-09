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
}
