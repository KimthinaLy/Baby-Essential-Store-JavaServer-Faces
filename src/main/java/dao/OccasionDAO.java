/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import model.Occasion;

/**
 *
 * @author Admin
 */
public interface OccasionDAO {

    List<Occasion> getAllOccasions() throws Exception;

    Occasion getOccasionById(int id) throws Exception;

    void insertOccasion(Occasion o) throws Exception;

    void updateOccasion(Occasion o) throws Exception;

    void deleteOccasion(int id) throws Exception;

}
