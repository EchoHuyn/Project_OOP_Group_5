package Model;

public class Seller {
    private String username;
    private String password;
    private String email;

    // Constructor
    public Seller(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters và Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Phương thức hiển thị thông tin Seller
    public void displaySellerInfo() {
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Email: " + email);
    }
}

