package Model;

public class Order {
    private String email; //Mã đơn khách hàng
    private Phones phones; // Danh sách các điện thoại trong đơn hàng
    private String purchaseTime; // Thời gian mua
    private String discount; // Mức giảm giá
    private String status; // Trạng thái xác nhận (e.g., "Confirmed", "Pending")

    // Constructor
    public Order(String email, Phones phones, String purchaseTime, String discount, String status) {
        this.email = email;
        this.phones = phones;
        this.purchaseTime = purchaseTime;
        this.discount = discount;
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    // Getter và Setter cho từng thuộc tính
    public Phones getPhones() {
        return phones;
    }

    public void setPhones(Phones phones) {
        this.phones = phones;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }  

    @Override
    public String toString() {
        return "Order{" + "email=" + email + ", phones=" + phones.getPhoneId() + ", purchaseTime=" + purchaseTime + ", discount=" + discount + ", status=" + status + '}';
    }
}

