/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.AddressDAO;
import dao.AddressDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.User;

/**
 *
 * @author Admin
 */
@Named
@SessionScoped
public class UserBean implements Serializable {

    private List<User> users;
    private List<User> selectedUsers;

    private User user;
    private Address address;

    private UserDAO userDAO = new UserDAOImpl();
    private AddressDAO addressDAO = new AddressDAOImpl();

    @PostConstruct
    public void init() {
        loadUsers();
    }

    public void loadUsers() {
        try {
            users = userDAO.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Open dialog for NEW user
    public void openNew() {
        user = new User();
        address = new Address();
    }

    // SAVE (Create or Update)
    public void save() {

        try {

            if (user.getUserId() == 0) {
                // CREATE USER
                int userId = userDAO.register(user);

                address.setUserId(userId);
                addressDAO.insertAddress(address);

            } else {
                // UPDATE USER
                userDAO.updateUser(user);

                if (address.getAddressId() == 0) {
                    address.setUserId(user.getUserId());
                    addressDAO.insertAddress(address);
                } else {
                    addressDAO.updateAddress(address);
                }
            }

            loadUsers();
            openNew();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Save Success!"));
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            e.getMessage(), null));
        }
    }

    // DELETE ONE USER
    public void deleteUser(int userId) {

        try {

            Address a = addressDAO.getAddressByUser(userId);

            if (a != null) {
                addressDAO.deleteAddress(a.getAddressId());
            }

            userDAO.deleteUser(userId);

            loadUsers();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Delete Success!"));

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Delete Failed", null));
        }
    }

    // DELETE MULTIPLE USERS
    public void deleteSelectedUsers() {

        try {

            for (User u : selectedUsers) {

                Address a = addressDAO.getAddressByUser(u.getUserId());

                if (a != null) {
                    addressDAO.deleteAddress(a.getAddressId());
                }

                userDAO.deleteUser(u.getUserId());
            }

            selectedUsers = new ArrayList<>();

            loadUsers();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Delete Success!"));
        } catch (Exception e) {
            e.printStackTrace();
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Delete Failed", null));
        }
    }

    // GETTERS / SETTERS
    public List<User> getUsers() {
        return users;
    }

    public List<User> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<User> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        try {
            this.address = addressDAO.getAddressByUser(user.getUserId());

            if (address == null) {
                address = new Address();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Address getAddress() {
        return address;
    }

}
