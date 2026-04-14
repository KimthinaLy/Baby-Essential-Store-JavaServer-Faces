/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.util.List;
import model.CartItem;
import model.Order;

/**
 *
 * @author Admin
 */
public interface OrderDAO {
    void placeOrderTransaction(Order order, List<CartItem> cartItems) throws Exception;
    
    int createOrder(Connection con, Order order) throws Exception;

    Order getOrderById(int orderId) throws Exception;

    List<Order> getOrdersByUser(int userId) throws Exception;

    void updateOrderStatus(int orderId, String status) throws Exception;

    void updatePaymentStatus(int orderId, String paymentStatus) throws Exception;
    
     void deleteOrderTransaction(int orderId) throws Exception;
     
     List<Order> getAllOrders() throws Exception;
}
