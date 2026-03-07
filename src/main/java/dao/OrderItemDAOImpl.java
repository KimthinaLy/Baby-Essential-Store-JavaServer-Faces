/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.OrderItem;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public void insertOrderItem(OrderItem item) throws Exception {

        String sql = """
        INSERT INTO order_items
        (order_id, product_id, quantity, product_name, price)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setString(4, item.getProductName());
            ps.setDouble(5, item.getPrice());

            ps.executeUpdate();
        }
    }

    @Override
    public List<OrderItem> getItemsByOrderId(int orderId) throws Exception {

        List<OrderItem> list = new ArrayList<>();

        String sql = "SELECT * FROM order_items WHERE order_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                OrderItem item = new OrderItem();

                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getDouble("price"));

                list.add(item);
            }
        }

        return list;
    }
}
