/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import java.sql.*;
import model.Product;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class CartItemDAOImpl implements CartItemDAO {

    @Override
    public List<CartItem> getCartItems(int cartId) throws Exception {

        List<CartItem> list = new ArrayList<>();

        String sql = """
        SELECT 
            ci.cart_item_id,
            ci.cart_id,
            ci.product_id,
            ci.quantity,
            p.name,
            p.price,
            p.image
        FROM cart_items ci
        JOIN products p ON ci.product_id = p.product_id
        WHERE ci.cart_id = ?
    """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CartItem item = new CartItem();

                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));

                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setImage(rs.getBytes("image"));

                item.setProduct(p);

                list.add(item);
            }
        }

        return list;
    }
    
    @Override
    public List<CartItem> getCartItemsForTransaction(Connection con, int cartId) throws Exception {

        List<CartItem> list = new ArrayList<>();

        String sql = """
        SELECT 
            ci.cart_item_id,
            ci.cart_id,
            ci.product_id,
            ci.quantity,
            p.name,
            p.price,
            p.image
        FROM cart_items ci
        JOIN products p ON ci.product_id = p.product_id
        WHERE ci.cart_id = ?
    """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CartItem item = new CartItem();

                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));

                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setImage(rs.getBytes("image"));

                item.setProduct(p);

                list.add(item);
            }
        }

        return list;
    }
    
    @Override
    public void addItem(Connection con, int cartId, int productId, int quantity) throws Exception {

        String sql = "INSERT INTO cart_items(cart_id, product_id, quantity) VALUES(?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            ps.executeUpdate();
        }
    }

    @Override
    public void updateQuantity(Connection con, int cartItemId, int quantity) throws Exception {

        String sql = "UPDATE cart_items SET quantity=? WHERE cart_item_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            ps.executeUpdate();
        }
    }
    
    @Override
    public List<CartItem> updateAndRefreshItems(int cartItemId, int newQty, int cartId) throws Exception {
    Connection con = null;
    try {
        con = DBUtil.getConnection();
        con.setAutoCommit(false); 

        this.updateQuantity(con, cartItemId, newQty);

        List<CartItem> freshItems = this.getCartItemsForTransaction(con, cartId);

        con.commit(); 
        return freshItems;
        
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
    public void removeItem(Connection con, int cartItemId) throws Exception {

        String sql = "DELETE FROM cart_items WHERE cart_item_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            ps.executeUpdate();
        }
    }
    
    @Override
    public List<CartItem> removeAndRefreshItems(int cartItemId, int cartId) throws Exception {
    Connection con = null;
    try {
        con = DBUtil.getConnection();
        con.setAutoCommit(false); 

        this.removeItem(con, cartItemId);

        List<CartItem> freshItems = this.getCartItemsForTransaction(con, cartId);

        con.commit(); 
        return freshItems;
        
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
    public List<CartItem> deleteSelectedTransaction(List<CartItem> itemsToDelete, int cartId) throws Exception {
    Connection con = null;
    try {
        con = DBUtil.getConnection();
        con.setAutoCommit(false); 

        String sql = "DELETE FROM cart_items WHERE cart_item_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (CartItem item : itemsToDelete) {
                ps.setInt(1, item.getCartItemId());
                ps.addBatch(); // Batching is faster for multiple deletes
            }
            ps.executeBatch();
        }

        List<CartItem> updatedList = this.getCartItemsForTransaction(con, cartId);

        con.commit(); 
        return updatedList;

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
    public void clearCart(int cartId) throws Exception {

        String sql = "DELETE FROM cart_items WHERE cart_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.executeUpdate();
        }
    }
    
    @Override
    public CartItem getItemForTransaction(Connection con, int cartId, int productId) throws Exception {

        CartItem item = null;

        String sql = "SELECT * FROM cart_items WHERE cart_id = ? AND product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                item = new CartItem();

                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
            }
        }
        return item;
    }
}
