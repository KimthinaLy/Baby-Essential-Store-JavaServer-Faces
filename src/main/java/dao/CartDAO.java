/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Cart;

/**
 *
 * @author Admin
 */
public interface CartDAO {

    Cart getCartByUserId(int userId) throws Exception;

    int createCart(int userId) throws Exception;

    void deleteCart(int cartId) throws Exception;
}
