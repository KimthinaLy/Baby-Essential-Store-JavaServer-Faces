/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.AddressDAO;
import dao.AddressDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import model.User;

import java.io.Serializable;
import model.Address;

/**
 *
 * @author Admin
 */
@Named
@SessionScoped
public class AuthBean implements Serializable {

    private User user = new User();
    private Address address = new Address();

    final private UserDAO userDAO = new UserDAOImpl();
    final private AddressDAO addressDAO = new AddressDAOImpl();

    public String login() {
        try {

            User loggedUser = userDAO.login(user.getEmail(), user.getPassword());

            if (loggedUser != null) {

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .put("user", loggedUser);

                if (loggedUser.getRole().equals("ADMIN")) {
                    return "/views/admin/dashboard?faces-redirect=true";
                }

                if (loggedUser.getRole().equals("MANAGER")) {
                    return "/views/manager/manage-products?faces-redirect=true";
                }

                if (loggedUser.getRole().equals("EMPLOYEE")) {
                    return "/views/employee/product-list?faces-redirect=true";
                }

                return "/views/customer/product?faces-redirect=true";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String register() {
        try {
            int userId = userDAO.register(user);
            
            address.setUserId(userId);

            addressDAO.insertAddress(address);
            
            return "/views/auth/login?faces-redirect=true";
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "/views/auth/login?faces-redirect=true";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    
}
