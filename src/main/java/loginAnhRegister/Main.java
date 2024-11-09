package loginAnhRegister;

import Model.Customer;
import Model.Order;
import Model.Phones;
import extensions.CsvFileHandler;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
//        Customer x = new Customer("John Doe", "Male", "19/9/1963",
//                "HaiDuong", "johndoe", "password123", "john@example.com");
//        x.displayCustomerInfo();
//        // Tạo danh sách Customer
//        ArrayList<Customer> customers = new ArrayList<>();
//        customers.add(x);
//        customers.add(new Customer("Jane Smith", "Female", "12/10/2004", "Los Angeles", "jane_smith", "pass456", "jane@example.com"));
//
//        // Ghi vào file CSV
//        CsvFileHandler.writeCustomersToCSV(customers, "accountCustomers.csv");

        // Đọc dữ liệu từ file CSV
//        ArrayList<Customer> customers2 = CsvFileHandler.readCustomersFromCSV("accountCustomers.csv");
//
//        // In ra thông tin các Customer
//        for (Customer customer : customers2) {
//            customer.displayCustomerInfo();
//            System.out.println(); // In dòng trống giữa các khách hàng
//        }
// Tạo danh sách Phones
//        ArrayList<Phones> phones = new ArrayList<>();
//        phones.add(new Phones("P001", "Apple", "iPhone 13", "999.99", "50"));
//        phones.add(new Phones("P002", "Samsung", "Galaxy S21", "799.66", "30"));
//
//        // Ghi vào file CSV
//        CsvFileHandler.writePhonesToCSV(phones, "phones.csv");
        // Đọc dữ liệu từ file CSV
//        ArrayList<Phones> phones = CsvFileHandler.readPhonesFromCSV("phones.csv");
//
//        // In ra thông tin các Phones
//        for (Phones phone : phones) {
//            phone.displayPhoneInfo();
//            System.out.println(); // In dòng trống giữa các điện thoại
//        }
//        ArrayList<Order> x = CsvFileHandler.readOrdersFromCSV();
//        for(Order k :  x){
//            System.out.println(k.toString());
//        }
        
        
        LoginForm.display();
    }
}
