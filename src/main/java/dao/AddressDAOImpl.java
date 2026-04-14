/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import model.Address;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class AddressDAOImpl implements AddressDAO {

    @Override
    public Address getAddressByUser(int userId) throws Exception {

        String sql = "SELECT * FROM address WHERE user_id=?";
        Address a = new Address();

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                a.setAddressId(rs.getInt("address_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setReceiverName(rs.getString("receiver_name"));
                a.setPhone(rs.getString("phone"));
                a.setStreet(rs.getString("street"));
                a.setCity(rs.getString("city"));
                a.setProvince(rs.getString("province"));
                a.setPostalCode(rs.getString("postal_code"));
            }
        }

        return a;
    }

    @Override
    public Address getAddressById(int addressId) throws Exception {

        String sql = "SELECT * FROM address WHERE address_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, addressId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Address a = new Address();

                a.setAddressId(rs.getInt("address_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setReceiverName(rs.getString("receiver_name"));
                a.setPhone(rs.getString("phone"));
                a.setStreet(rs.getString("street"));
                a.setCity(rs.getString("city"));
                a.setProvince(rs.getString("province"));
                a.setPostalCode(rs.getString("postal_code"));

                return a;
            }
        }

        return null;
    }
    
    @Override
    public void insertAddressInTransaction(Connection con, Address address) throws Exception{
        String sql = "INSERT INTO address (user_id, receiver_name, phone, street, city, province, postal_code) VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getReceiverName());
            ps.setString(3, address.getPhone());
            ps.setString(4, address.getStreet());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getProvince());
            ps.setString(7, address.getPostalCode());

            ps.executeUpdate();
        }
    }

    @Override
    public void insertAddress(Address address) throws Exception {

        String sql = """
        INSERT INTO address
        (user_id, receiver_name, phone, street, city, province, postal_code)
        VALUES(?,?,?,?,?,?,?)
        """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getReceiverName());
            ps.setString(3, address.getPhone());
            ps.setString(4, address.getStreet());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getProvince());
            ps.setString(7, address.getPostalCode());

            ps.executeUpdate();
        }
    }

    @Override
    public void updateAddress(Address address) throws Exception {

        String sql = """
        UPDATE address
        SET receiver_name=?, phone=?, street=?, city=?, province=?, postal_code=?
        WHERE address_id=?
        """;

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, address.getReceiverName());
            ps.setString(2, address.getPhone());
            ps.setString(3, address.getStreet());
            ps.setString(4, address.getCity());
            ps.setString(5, address.getProvince());
            ps.setString(6, address.getPostalCode());
            ps.setInt(7, address.getAddressId());

            ps.executeUpdate();
        }
    }
    
    @Override
    public  void updateAddressInTransaction(Connection con, Address address) throws Exception{
        String sql = "UPDATE address SET receiver_name=?, phone=?, street=?, city=?, province=?, postal_code=? WHERE address_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, address.getReceiverName());
            ps.setString(2, address.getPhone());
            ps.setString(3, address.getStreet());
            ps.setString(4, address.getCity());
            ps.setString(5, address.getProvince());
            ps.setString(6, address.getPostalCode());
            ps.setInt(7, address.getAddressId());

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAddress(int addressId) throws Exception {

        String sql = "DELETE FROM address WHERE address_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, addressId);
            ps.executeUpdate();
        }
    }
}
