package Model;

public class Order {
    private Phones phones; // Danh sách các điện thoại trong đơn hàng
    private String purchaseTime; // Thời gian mua
    private String discount; // Mức giảm giá
    private String status; // Trạng thái xác nhận (e.g., "Confirmed", "Pending")

    // Constructor
    public Order(Phones phones, String purchaseTime, String discount, String status) {
        this.phones = phones;
        this.purchaseTime = purchaseTime;
        this.discount = discount;
        this.status = status;
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

    // Phương thức toString để hiển thị thông tin của đơn hàng
    @Override
    public String toString() {
        return "Order{" +
                "phones=" + phones +
                ", purchaseTime='" + purchaseTime + '\'' +
                ", discount=" + discount +
                ", status='" + status + '\'' +
                '}';
    }
}

