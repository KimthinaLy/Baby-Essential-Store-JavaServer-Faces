/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public int createOrder(Order order) throws Exception {

        String sql = """
        INSERT INTO orders
        (user_id, address_id, total_amount, order_status, payment_method, payment_status)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getAddressId());
            ps.setDouble(3, order.getTotalAmount());
            ps.setString(4, order.getOrderStatus());
            ps.setString(5, order.getPaymentMethod());
            ps.setString(6, order.getPaymentStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    @Override
    public Order getOrderById(int orderId) throws Exception {

        String sql = "SELECT * FROM orders WHERE order_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Order order = new Order();

                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setAddressId(rs.getInt("address_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setPaymentStatus(rs.getString("payment_status"));

                return order;
            }
        }

        return null;
    }

    @Override
    public List<Order> getOrdersByUser(int userId) throws Exception {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE user_id=? ORDER BY order_date DESC";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Order order = new Order();

                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setAddressId(rs.getInt("address_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setPaymentStatus(rs.getString("payment_status"));

                orders.add(order);
            }
        }

        return orders;
    }

    @Override
    public void updateOrderStatus(int orderId, String status) throws Exception {

        String sql = "UPDATE orders SET order_status=? WHERE order_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            ps.executeUpdate();
        }
    }

    @Override
    public void updatePaymentStatus(int orderId, String paymentStatus) throws Exception {

        String sql = "UPDATE orders SET payment_status=? WHERE order_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setInt(2, orderId);

            ps.executeUpdate();
        }
    }
}
