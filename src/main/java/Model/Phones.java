package Model;

public class Phones {
    // Các thuộc tính
    private String phoneId;
    private String brand;
    private String model;
    private String price;
    private String stockQuantity;

    // Constructor
    public Phones(String phoneId, String brand, String model, String price, String stockQuantity) {
        this.phoneId = phoneId;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getter và Setter
    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    // Phương thức hiển thị thông tin của Phone
    public void displayPhoneInfo() {
        System.out.println("Phone ID: " + phoneId);
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Price: $" + price);
        System.out.println("Stock Quantity: " + stockQuantity);
    }
}

