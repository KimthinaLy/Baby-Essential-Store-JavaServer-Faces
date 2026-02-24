/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.Product;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public List<Product> getAllProducts() throws Exception {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productList.add(new Product(rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("qty_on_hand")));
            }
        }
        return productList;
    }

    @Override
    public void insertProduct(Product p) throws Exception {
        String sql = "INSERT INTO products(product_id,name,price,qty_on_hand) VALUES(?,?,?,?)";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getQuantityOnHand());

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteProduct(int id) throws Exception {
        String sql = "DELETE FROM products WHERE product_id=?";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateProduct(Product p) throws Exception {
        String sql = "UPDATE products SET name=?, price=?, qty_on_hand=? WHERE product_id=?";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getQuantityOnHand());
            ps.setInt(4, p.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public Product getProductById(int id) throws Exception {
        String sql = "SELECT * FROM products WHERE product_id=?";
        Product p = new Product();

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantityOnHand(rs.getInt("qty_on_hand"));
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Product> searchByName(String keyword) throws Exception {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantityOnHand(rs.getInt("qty_on_hand"));
                
                productList.add(p);
            }
        }
        return productList;
    }
}
