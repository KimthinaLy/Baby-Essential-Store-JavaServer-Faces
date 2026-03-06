/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import model.Category;

/**
 *
 * @author Admin
 */
public interface CategoryDAO {

    List<Category> getAllCategories() throws Exception;

    Category getCategoryById(int id) throws Exception;

    void insertCategory(Category c) throws Exception;

    void updateCategory(Category c) throws Exception;

    void deleteCategory(int id) throws Exception;
}
