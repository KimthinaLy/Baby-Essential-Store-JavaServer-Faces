/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import model.Address;

/**
 *
 * @author Admin
 */
public interface AddressDAO {
    
     Address getAddressByUser(int userId) throws Exception;
     
     Address getAddressById(int addressId) throws Exception;

    void insertAddress(Address address) throws Exception;

    void updateAddress(Address address) throws Exception;

    void deleteAddress(int addressId) throws Exception;
    
    void insertAddressInTransaction(Connection con, Address address) throws Exception;
    
    void updateAddressInTransaction(Connection con, Address address) throws Exception;
}
