package Model;

public class Customer {
    // Các thuộc tính
    private String name;
    private String gender;
    private String birthDate;
    private String hometown;
    private String username;
    private String password;
    private String email;

    // Constructor
    public Customer(String name, String gender, String birthDate, String hometown, 
                    String username, String password, String email) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.hometown = hometown;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getter và Setter cho các thuộc tính
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

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

    // Phương thức hiển thị thông tin của Customer
    public void displayCustomerInfo() {
        System.out.println("Name: " + name);
        System.out.println("Gender: " + gender);
        System.out.println("Birth Date: " + birthDate);
        System.out.println("Hometown: " + hometown);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Email: " + email);
    }
}

