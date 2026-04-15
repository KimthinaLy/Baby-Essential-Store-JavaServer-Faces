/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import jakarta.validation.constraints.*;

/**
 *
 * @author Admin
 */
public class User {
    private int userId;
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name must not contain numbers or special characters")
    private String fullName;
    
    @Email(message = "Please provide a valid email address")
    private String email;
    
    private String password;
    
    @Pattern(regexp = "^\\(\\d{3}\\) \\d{3}-\\d{4}$", message = "Invalid phone format")
    private String phone;
    private String role;

    public User() {
    }

    public User(int userId, String fullName, String email, String password, String phone, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
