/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Product;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ProductDAO {
    List<Product> getAllProducts() throws Exception;
    void insertProduct(Product p) throws Exception;
    void deleteProduct(int id) throws Exception;
    void updateProduct(Product p) throws Exception;
    Product getProductById(int id) throws Exception;
    List<Product> searchByName(String keyword) throws Exception;
    List<Product> getProductsByCategory(int categoryId) throws Exception;
    List<Product> getProductsByOccasion(int occasionId) throws Exception;
}
