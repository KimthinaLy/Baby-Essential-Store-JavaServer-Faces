/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.util.List;
import model.CartItem;

/**
 *
 * @author Admin
 */
public interface CartItemDAO {

    List<CartItem> getCartItems(int cartId) throws Exception;
    
    List<CartItem> getCartItemsForTransaction(Connection con, int cartId) throws Exception;

    void addItem(Connection con, int cartId, int productId, int quantity) throws Exception;

    void updateQuantity(Connection con, int cartItemId, int quantity) throws Exception;

    void removeItem(Connection con, int cartItemId) throws Exception;

    void clearCart(int cartId) throws Exception;

    //CartItem getItem(int cartId, int productId) throws Exception;
    
    CartItem getItemForTransaction(Connection con, int cartId, int productId) throws Exception;
    
    List<CartItem> updateAndRefreshItems(int cartItemId, int newQty, int cartId) throws Exception;
    
    List<CartItem> removeAndRefreshItems(int cartItemId, int cartId) throws Exception;
    
    List<CartItem> deleteSelectedTransaction(List<CartItem> itemsToDelete, int cartId) throws Exception;

}
