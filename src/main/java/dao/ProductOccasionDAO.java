/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;

/**
 *
 * @author Admin
 */
public interface ProductOccasionDAO {
    void insertProductOccasion(int productId, int occasionId) throws Exception;

    void insertProductOccasion(int productId, List<Integer> occasionIds) throws Exception;

    List<Integer> getOccasionIdsByProductId(int productId) throws Exception;

    void deleteProductOccasion(int productId, int occasionId) throws Exception;

    void deleteAllOccasionsByProductId(int productId) throws Exception;

    void updateProductOccasions(int productId, List<Integer> newOccasionIds) throws Exception;
}
