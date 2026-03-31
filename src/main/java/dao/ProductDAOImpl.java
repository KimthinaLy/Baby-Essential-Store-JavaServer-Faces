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
        String sql = """
            SELECT p.*, c.category_name
            FROM products p
            JOIN categories c 
            ON p.category_id = c.category_id
            """;

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                Product p = new Product();

                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantityOnHand(rs.getInt("qty_on_hand"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                p.setImage(rs.getBytes("image"));

                // fetch occasions for this product
                p.setOccasions(getOccasionsByProductId(p.getProductId()));

                productList.add(p);
            }
        }
        return productList;
    }

    @Override
    public void insertProduct(Product p) throws Exception {
        String sql = "INSERT INTO products(name,price,qty_on_hand,category_id, description, image ) VALUES(?,?,?,?,?,?)";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getQuantityOnHand());
            ps.setInt(4, p.getCategoryId());
            ps.setString(5, p.getDescription());
            // Handle the byte array
            if (p.getImage() != null) {
                ps.setBytes(6, p.getImage());
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }

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
        String sql = "UPDATE products SET name=?, price=?, qty_on_hand=?, category_id=?,description=?,image=? WHERE product_id=?";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getQuantityOnHand());
            ps.setInt(4, p.getCategoryId());
            ps.setString(5, p.getDescription());
            ps.setBytes(6, p.getImage());
            ps.setInt(7, p.getProductId());

            ps.executeUpdate();
        }
    }

    @Override
    public Product getProductById(int id) throws Exception {
        String sql = """
            SELECT p.*, c.category_name
            FROM products p
            JOIN categories c 
            ON p.category_id = c.category_id
            WHERE p.product_id = ?
            """;
        Product p = new Product();

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantityOnHand(rs.getInt("qty_on_hand"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setDescription(rs.getString("description"));
                p.setImage(rs.getBytes("image"));
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Product> searchByName(String keyword) throws Exception {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE ?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantityOnHand(rs.getInt("qty_on_hand"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setDescription(rs.getString("description"));
                p.setImage(rs.getBytes("image"));

                productList.add(p);
            }
        }
        return productList;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) throws Exception {
        List<Product> productList = new ArrayList<>();

        String sql = """
            SELECT p.*, c.category_name
            FROM products p
            JOIN categories c
            ON p.category_id = c.category_id
            WHERE p.category_id = ?
            """;

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = new Product();

                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantityOnHand(rs.getInt("qty_on_hand"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                p.setImage(rs.getBytes("image"));

                p.setOccasions(getOccasionsByProductId(p.getProductId()));

                productList.add(p);
            }
        }

        return productList;
    }

    @Override
    public List<Product> getProductsByOccasion(int occasionId) throws Exception {

        List<Product> productList = new ArrayList<>();

        String sql = """
        SELECT p.*, c.category_name
        FROM products p
        JOIN categories c 
        ON p.category_id = c.category_id
        JOIN product_occasions po
        ON p.product_id = po.product_id
        WHERE po.occasion_id = ?
        """;

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, occasionId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantityOnHand(rs.getInt("qty_on_hand"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setCategoryName(rs.getString("category_name"));
                p.setImage(rs.getBytes("image"));

                // load occasions list
                p.setOccasions(getOccasionsByProductId(p.getProductId()));

                productList.add(p);
            }
        }

        return productList;
    }

    public List<String> getOccasionsByProductId(int productId) throws Exception {

        List<String> occasions = new ArrayList<>();

        String sql = """
        SELECT o.occasion_name
        FROM occasions o
        JOIN product_occasions po 
        ON o.occasion_id = po.occasion_id
        WHERE po.product_id = ?
        """;

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                occasions.add(rs.getString("occasion_name"));
            }
        }

        return occasions;
    }
}
