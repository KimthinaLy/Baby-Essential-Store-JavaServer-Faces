/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import model.Cart;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public Cart getCartByUserId(int userId) throws Exception {

        String sql = "SELECT * FROM carts WHERE user_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getInt("user_id"));
                return cart;
            }
        }

        return null;
    }

    @Override
    public int createCart(int userId) throws Exception {

        String sql = "INSERT INTO carts(user_id) VALUES(?)";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    @Override
    public void deleteCart(int cartId) throws Exception {

        String sql = "DELETE FROM carts WHERE cart_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.executeUpdate();
        }
    }
}
