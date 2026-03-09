/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import dao.CartItemDAO;
import dao.CartItemDAOImpl;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.Cart;
import model.CartItem;
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
    private CartDAO cartDAO = new CartDAOImpl();
    private CartItemDAO cartItemDAO = new CartItemDAOImpl();

    public String addToCart(int productId) {

        FacesContext context = FacesContext.getCurrentInstance();

        User user = (User) context
                .getExternalContext()
                .getSessionMap()
                .get("user");

        if (user == null) {
            return "/views/auth/login?faces-redirect=true";
        }

        try {

            int userId = user.getUserId();

            Cart cart = cartDAO.getCartByUserId(userId);

            if (cart == null) {
                int cartId = cartDAO.createCart(userId);
                cart = new Cart(cartId, userId);
            }

            CartItem item = cartItemDAO.getItem(cart.getCartId(), productId);

            if (item != null) {

                int newQty = item.getQuantity() + 1;

                cartItemDAO.updateQuantity(item.getCartItemId(), newQty);

            } else {

                cartItemDAO.addItem(cart.getCartId(), productId, 1);
            }
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Added to cart"));

            }catch (Exception e) {
            e.printStackTrace();
        }

            return null;
        }

    

    public List<Product> getCart() {
        return cart;
    }

}
