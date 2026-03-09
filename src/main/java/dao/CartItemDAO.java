/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import model.CartItem;

/**
 *
 * @author Admin
 */
public interface CartItemDAO {

    List<CartItem> getCartItems(int cartId) throws Exception;

    void addItem(int cartId, int productId, int quantity) throws Exception;

    void updateQuantity(int cartItemId, int quantity) throws Exception;

    void removeItem(int cartItemId) throws Exception;

    void clearCart(int cartId) throws Exception;
    CartItem getItem(int cartId, int productId) throws Exception;

}
