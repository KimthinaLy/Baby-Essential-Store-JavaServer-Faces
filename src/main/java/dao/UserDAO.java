/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.User;

/**
 *
 * @author Admin
 */
public interface UserDAO {
    User login(String email, String password) throws Exception;
    int register(User user) throws Exception;
    User findByEmail(String email) throws Exception;
    User findById(int id) throws Exception;
}
