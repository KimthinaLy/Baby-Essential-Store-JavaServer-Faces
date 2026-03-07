/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import model.Order;

/**
 *
 * @author Admin
 */
public interface OrderDAO {
    
    int createOrder(Order order) throws Exception;

    Order getOrderById(int orderId) throws Exception;

    List<Order> getOrdersByUser(int userId) throws Exception;

    void updateOrderStatus(int orderId, String status) throws Exception;

    void updatePaymentStatus(int orderId, String paymentStatus) throws Exception;
}
