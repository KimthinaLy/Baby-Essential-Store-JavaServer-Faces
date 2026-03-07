/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import model.OrderItem;

/**
 *
 * @author Admin
 */
public interface OrderItemDAO {
    void insertOrderItem(OrderItem item) throws Exception;

    List<OrderItem> getItemsByOrderId(int orderId) throws Exception;
}
