/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.User;

/**
 *
 * @author Admin
 */
@Named
@SessionScoped
public class CartBean implements Serializable {

    private List<Product> cart = new ArrayList<>();

    public String addToCart(Product p) {

        FacesContext context = FacesContext.getCurrentInstance();

        User user = (User) context
                .getExternalContext()
                .getSessionMap()
                .get("user");

        if (user == null) {
            return "/views/auth/login?faces-redirect=true";
        }
        cart.add(p);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Added to cart"));
        return null;
    }

    public List<Product> getCart() {
        return cart;
    }

}
