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
import jakarta.faces.application.FacesMessage;
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
                user = loggedUser;
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getSessionMap()
                        .put("user", loggedUser);

                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Login Success!"));

                if (loggedUser.getRole().equals("ADMIN")) {
                    return "/views/admin/manage-users?faces-redirect=true";
                }

                if (loggedUser.getRole().equals("MANAGER")) {
                    return "/views/manager/manage-products?faces-redirect=true";
                }

                if (loggedUser.getRole().equals("EMPLOYEE")) {
                    return "/views/employee/view-orders?faces-redirect=true";
                }

                return "/views/customer/product?faces-redirect=true";
            }else{
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Login Fail! Incorrect password or email.", null));
                return null;
            }
        } catch (IllegalArgumentException e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Security Error", "Old password format detected. Please contact admin."));
        return null;
    }
        
        catch (Exception e) {
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            e.getMessage(), null));
            e.printStackTrace();
            return null;
        }
    }

    public String register() {
        try {
            int userId = userDAO.register(user);
            
            address.setUserId(userId);

            addressDAO.insertAddress(address);
            
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Registration Success!"));
            
            return "/views/auth/login?faces-redirect=true";
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Registration Fail.", null));
            return null;
        }       
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        user = null;

        return "/views/customer/product?faces-redirect=true";
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
