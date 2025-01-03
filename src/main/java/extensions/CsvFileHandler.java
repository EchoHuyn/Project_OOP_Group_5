package extensions;

import Model.Customer;
import Model.Order;
import Model.Phones;
import Model.Seller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvFileHandler {
    
    //Xu li CSV Customer  
    public static void writeCustomersToCSV(ArrayList<Customer> customers, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Ghi tiêu đề cột
            writer.write("Name,Gender,BirthDate,Hometown,Username,Password,Email");
            writer.newLine();

            // Ghi từng customer vào file
            for (Customer customer : customers) {
                writer.write(customer.getName() + "," +
                        customer.getGender() + "," +
                        customer.getBirthDate() + "," +
                        customer.getHometown() + "," +
                        customer.getUsername() + "," +
                        customer.getPassword() + "," +
                        customer.getEmail());
                writer.newLine();
            }

            System.out.println("Ghi dữ liệu vào file CSV thành công!");

        } catch (IOException e) {
            System.out.println("Lỗi khi ghi vào file CSV: " + e.getMessage());
        }
    }
    
    public static ArrayList<Customer> readCustomersFromCSV(String fileName) {
        ArrayList<Customer> customers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isHeader = true; // Bỏ qua tiêu đề cột

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Bỏ qua dòng đầu tiên (tiêu đề)
                }

                String[] values = line.split(",");

                // Kiểm tra xem file có đúng định dạng không
                if (values.length == 7) {
                    String name = values[0];
                    String gender = values[1];
                    String birthDate = values[2];
                    String hometown = values[3];
                    String username = values[4];
                    String password = values[5];
                    String email = values[6];

                    // Tạo đối tượng Customer và thêm vào ArrayList
                    Customer customer = new Customer(name, gender, birthDate, hometown, username, password, email);
                    customers.add(customer);
                }
            }

            System.out.println("Đọc dữ liệu từ file CSV thành công!");

        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        }

        return customers;
    }
    
    //Xu li CSV Phones
    public static void writePhonesToCSV(ArrayList<Phones> phones, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Ghi tiêu đề cột
            writer.write("Phone ID,Brand,Model,Price,Stock Quantity");
            writer.newLine();

            // Ghi từng phone vào file
            for (Phones phone : phones) {
                writer.write(phone.getPhoneId() + "," +
                        phone.getBrand() + "," +
                        phone.getModel() + "," +
                        phone.getPrice() + "," +
                        phone.getStockQuantity());
                writer.newLine();
            }

            System.out.println("Ghi dữ liệu vào file CSV thành công!");

        } catch (IOException e) {
            System.out.println("Lỗi khi ghi vào file CSV: " + e.getMessage());
        }
    }
    
    public static ArrayList<Phones> readPhonesFromCSV(String fileName) {
        ArrayList<Phones> phones = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isHeader = true; // Bỏ qua tiêu đề cột

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Bỏ qua dòng đầu tiên (tiêu đề)
                }

                String[] values = line.split(",");

                // Kiểm tra xem file có đúng định dạng không
                if (values.length == 5) {
                    String phoneId = values[0];
                    String brand = values[1];
                    String model = values[2];
                    String price = (values[3]);
                    String stockQuantity = (values[4]);

                    // Tạo đối tượng Phones và thêm vào ArrayList
                    Phones phone = new Phones(phoneId, brand, model, price, stockQuantity);
                    phones.add(phone);
                }
            }

            System.out.println("Đọc dữ liệu từ file CSV thành công!");

        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        }

        return phones;
    }
    
    //Đọc ghi file csv của Admin
    public static void writeSellersToCSV(ArrayList<Seller> sellers, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Ghi tiêu đề cột
            writer.write("Username,Password,Email");
            writer.newLine();

            // Ghi từng seller vào file
            for (Seller seller : sellers) {
                writer.write(seller.getUsername() + "," +
                        seller.getPassword() + "," +
                        seller.getEmail());
                writer.newLine();
            }

            System.out.println("Ghi dữ liệu vào file CSV thành công!");

        } catch (IOException e) {
            System.out.println("Lỗi khi ghi vào file CSV: " + e.getMessage());
        }
    }
    
    public static ArrayList<Seller> readSellersFromCSV(String fileName) {
        ArrayList<Seller> sellers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isHeader = true; // Bỏ qua tiêu đề cột

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Bỏ qua dòng đầu tiên (tiêu đề)
                }

                String[] values = line.split(",");

                // Kiểm tra xem file có đúng định dạng không
                if (values.length == 3) {
                    String username = values[0];
                    String password = values[1];
                    String email = values[2];

                    // Tạo đối tượng Seller và thêm vào ArrayList
                    Seller seller = new Seller(username, password, email);
                    sellers.add(seller);
                }
            }

            System.out.println("Đọc dữ liệu từ file CSV thành công!");

        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        }

        return sellers;
    }
    
    //Xử lý CSV của Order
    public static void writeOrderToCSV(Order order) {
        String logFileName = "unconfirmOrders.csv";
        File orderFile = new File(logFileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFile, true))) {
            // Ghi tiêu đề cột nếu file trống
            if (orderFile.length() == 0) {
                writer.write("Email,Phone ID,Phone Brand,Phone Model,Phone Price,Phone Stock Quantity,Purchase Time,Discount,Status");
                writer.newLine();
            }

            // Ghi đơn hàng vào file
            writer.write(order.getEmail().replace("@gmail.com", "") + ","
                    + order.getPhones().getPhoneId() + ","
                     + order.getPhones().getBrand() + ","
                     + order.getPhones().getModel() + ","
                     + order.getPhones().getPrice() + ","
                     + order.getPhones().getStockQuantity() + ","
                    + order.getPurchaseTime() + ","
                    + order.getDiscount() + ","
                    + order.getStatus());
            writer.newLine();
            
            System.out.println("Đã ghi đơn hàng thành công vào file CSV: " + logFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<Order> readOrdersFromCSV() {
        ArrayList<Order> orders = new ArrayList<>();
        String logFileName = "unconfirmOrders.csv";
        File orderFile = new File(logFileName);

        // Kiểm tra nếu file không tồn tại
        if (!orderFile.exists()) {
            System.out.println("File không tồn tại: " + logFileName);
            return orders; // Trả về danh sách trống nếu file không tồn tại
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
            String line;
            // Đọc tiêu đề cột (bỏ qua dòng đầu tiên)
            reader.readLine();

            // Đọc từng dòng dữ liệu
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                // Đảm bảo dòng có đúng số lượng cột
                if (data.length == 9) {
                    // Tạo đối tượng Phones từ dữ liệu đọc được
                    Phones phone = new Phones(data[1], data[2], data[3], data[4], data[5]);
                    
                    // Tạo đối tượng Order từ dữ liệu đọc được
                    Order order = new Order(
                        data[0],
                        phone,
                        data[6],  // purchaseTime
                        data[7],  // discount
                        data[8]   // status
                    );
                    // Thêm đơn hàng vào danh sách
                    orders.add(order);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return orders;
    }
    
    public static void writeOrdersToUnconfirmOrders(ArrayList<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("Danh sách đơn hàng trống, không có gì để ghi.");
            return;
        }
       
        String fileName = "unconfirmOrders.csv";
        File orderFile = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFile))) {
            // Ghi tiêu đề cho file CSV
            writer.write("Email,Phone ID,Brand,Model,Price,Quantity,Purchase Time,Discount,Status");
            writer.newLine();

            // Ghi từng đơn hàng vào file
            for (Order order : orders) {
                writer.write(order.getEmail() + ","
                        + order.getPhones().getPhoneId() + ","
                        + order.getPhones().getBrand() + ","
                        + order.getPhones().getModel() + ","
                        + order.getPhones().getPrice() + ","
                        + order.getPhones().getStockQuantity() + ","
                        + order.getPurchaseTime() + ","
                        + order.getDiscount() + ","
                        + order.getStatus());
                writer.newLine();
            }

            System.out.println("Ghi đè dữ liệu thành công vào file: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
