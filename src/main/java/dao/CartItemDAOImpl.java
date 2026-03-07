/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import java.sql.*;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class CartItemDAOImpl implements CartItemDAO {

    @Override
    public List<CartItem> getCartItems(int cartId) throws Exception {

        List<CartItem> list = new ArrayList<>();

        String sql = "SELECT * FROM cart_items WHERE cart_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CartItem item = new CartItem();

                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));

                list.add(item);
            }
        }

        return list;
    }

    @Override
    public void addItem(int cartId, int productId, int quantity) throws Exception {

        String sql = "INSERT INTO cart_items(cart_id, product_id, quantity) VALUES(?,?,?)";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            ps.executeUpdate();
        }
    }

    @Override
    public void updateQuantity(int cartItemId, int quantity) throws Exception {

        String sql = "UPDATE cart_items SET quantity=? WHERE cart_item_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            ps.executeUpdate();
        }
    }

    @Override
    public void removeItem(int cartItemId) throws Exception {

        String sql = "DELETE FROM cart_items WHERE cart_item_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartItemId);
            ps.executeUpdate();
        }
    }

    @Override
    public void clearCart(int cartId) throws Exception {

        String sql = "DELETE FROM cart_items WHERE cart_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.executeUpdate();
        }
    }
}
