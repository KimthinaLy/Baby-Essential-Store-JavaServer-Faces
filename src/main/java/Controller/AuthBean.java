/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.Serializable;
import model.Address;
import util.JwtUtil;

/**
 *
 * @author Admin
 */
@Named
@SessionScoped
public class AuthBean implements Serializable {
    @Inject
        private JwtUtil jwtUtil;

    private User user = new User();
    private Address address = new Address();

    final private UserDAO userDAO = new UserDAOImpl();

    public String login() {
        try {
            User loggedUser = userDAO.login(user.getEmail(), user.getPassword());
            if (loggedUser != null) {
                user = loggedUser;
                String token = jwtUtil.generateToken(loggedUser);

                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                        .getExternalContext().getResponse();
                Cookie jwtCookie = new Cookie("customer_auth", token);
                jwtCookie.setHttpOnly(true); // Protection against XSS
                jwtCookie.setPath("/");      // Available for the whole site
                jwtCookie.setMaxAge(3600);   // 1 hour
                response.addCookie(jwtCookie);

                if (!"CUSTOMER".equals(loggedUser.getRole())) {
                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                            .getExternalContext().getSession(true);
                    session.setAttribute("staff", loggedUser);
                }

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
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Login Fail! Incorrect password or email.", null));
                return null;
            }
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Security Error", "Old password format detected. Please contact admin."));
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            e.getMessage(), null));
            e.printStackTrace();
            return null;
        }
    }

    public String register() {
        try {
            userDAO.registerWithAddress(user, address);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
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

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();
        Cookie cookie = new Cookie("customer_auth", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

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
