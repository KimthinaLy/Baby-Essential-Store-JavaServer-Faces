/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import dao.CartItemDAO;
import dao.CartItemDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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

    final private CartDAO cartDAO = new CartDAOImpl();
    final private CartItemDAO cartItemDAO = new CartItemDAOImpl();
    //final private ProductDAO productDAO = new ProductDAOImpl();

//    private List<Product> cartList = new ArrayList<>();
    private List<CartItem> cartItems;
    private List<CartItem> selectedItems;

    public String addToCart(int productId) {
        FacesContext context = FacesContext.getCurrentInstance();
        User user = (User) context
                .getExternalContext()
                .getSessionMap()
                .get("user");
        //User user = authBean.getUser();
        if (user == null) {
            return "/views/auth/login?faces-redirect=true";
        }

        try {
            Cart cart = cartDAO.getCartByUserId(user.getUserId());

            if (cart == null) {
                int cartID = cartDAO.createCart(user.getUserId());
                cart = new Cart(cartID, user.getUserId());
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    public List<Product> getCartList() {
        return cartList;
    }*/
    public List<CartItem> getCartItems() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();

            User user = (User) context.getExternalContext().getSessionMap().get("user");
            if (user == null) {
                throw new IllegalStateException("No user in session");
            }

            Cart cart = cartDAO.getCartByUserId(user.getUserId());
            if (cart == null) {
                return Collections.emptyList(); // or create a new cart
            }

            cartItems = cartItemDAO.getCartItems(cart.getCartId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public void setSelectedItems(List<CartItem> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public List<CartItem> getSelectedItems() {
        return selectedItems;
    }

    public void deleteSelected() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = (User) context
                    .getExternalContext()
                    .getSessionMap()
                    .get("user");
            int userId = user.getUserId();

            Cart cart = cartDAO.getCartByUserId(userId);
            for (CartItem item : selectedItems) {
                cartItemDAO.removeItem(item.getCartItemId());
            }

            cartItems = cartItemDAO.getCartItems(cart.getCartId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItem(int itemId, int qty) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = (User) context
                    .getExternalContext()
                    .getSessionMap()
                    .get("user");
            int userId = user.getUserId();

            Cart cart = cartDAO.getCartByUserId(userId);

            cartItemDAO.updateQuantity(itemId, qty);
            cartItems = cartItemDAO.getCartItems(cart.getCartId());

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Update Failed", null));
        }
    }

    public String checkoutSelected() {

        if (selectedItems == null || selectedItems.isEmpty()) {
            return null;
        }
        return "/views/customer/checkout?faces-redirect=true";

    }

    public void removeItem(int cartItemId) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            User user = (User) context
                    .getExternalContext()
                    .getSessionMap()
                    .get("user");
            int userId = user.getUserId();

            Cart cart = cartDAO.getCartByUserId(userId);
            cartItemDAO.removeItem(cartItemId);
            cartItems = cartItemDAO.getCartItems(cart.getCartId());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Delete Failed", null));
        }
    }

    public double getSelectedTotal() {

        double total = 0;

        if (selectedItems != null) {
            for (CartItem item : selectedItems) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        return total;
    }
}
