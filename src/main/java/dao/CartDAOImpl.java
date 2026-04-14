/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import model.Cart;
import util.DBUtil;
import model.CartItem;

/**
 *
 * @author Admin
 */
public class CartDAOImpl implements CartDAO {
    final private CartItemDAO cartItemDAO = new CartItemDAOImpl();
    
    @Override
    public void addItemToCartTransaction(int userId, int productId) throws Exception {
    Connection con = null;
    try {
        con = DBUtil.getConnection();
        con.setAutoCommit(false); 

        Cart cart = getCartByUserIdForTransaction(con, userId);
        int cartId;
        
        if (cart == null) {
            cartId = createCart(con, userId);
        } else {
            cartId = cart.getCartId();
        }

        CartItem item = cartItemDAO.getItemForTransaction(con, cartId, productId);

        if (item != null) {
            cartItemDAO.updateQuantity(con, item.getCartItemId(), item.getQuantity() + 1);
        } else {
            cartItemDAO.addItem(con, cartId, productId, 1);
        }

        con.commit(); 
    } catch (Exception e) {
        if (con != null) con.rollback(); 
        throw e;
    } finally {
        if (con != null) {
            con.setAutoCommit(true);
            con.close();
        }
    }
}
    
      @Override
    public Cart getCartByUserIdForTransaction(Connection con, int userId) throws Exception {

        String sql = "SELECT * FROM carts WHERE user_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

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
    public int createCart(Connection con, int userId) throws Exception {

        String sql = "INSERT INTO carts(user_id) VALUES(?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            
            return rs.next()? rs.getInt(1): 0;
        }
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
