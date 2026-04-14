/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.util.List;
import model.User;
import model.Address;

/**
 *
 * @author Admin
 */
public interface UserDAO {
    User login(String email, String password) throws Exception;
    int register(User user) throws Exception;
    User findByEmail(String email) throws Exception;
    User findById(int id) throws Exception;
    List<User> getAllUsers() throws Exception;
    void updateUser(User user) throws Exception;
    void deleteUser(int userId) throws Exception;
    void registerWithAddress(User user, Address address) throws Exception;
    int registerUserInTransaction(Connection con, User user) throws Exception;
    void updateUserInTransaction(Connection con, User user) throws Exception;
}
