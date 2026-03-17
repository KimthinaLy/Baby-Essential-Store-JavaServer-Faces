/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public int register(User user) throws Exception {

        String sql = "INSERT INTO user(full_name,email,password,phone,role) VALUES(?,?,?,?,?)";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, "CUSTOMER");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    @Override
    public User login(String email, String password) throws Exception {
        String sql = "SELECT * FROM user WHERE email=? AND password=?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setPhone(rs.getString("phone"));

                return user;
            }
        }

        return null;
    }

    @Override
    public User findByEmail(String email) throws Exception {

        String sql = "SELECT * FROM user WHERE email=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                return user;
            }
        }

        return null;
    }

    @Override
    public User findById(int id) throws Exception {
        String sql = "SELECT * FROM user WHERE user_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {

                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }

        return users;
    }

    @Override
    public void updateUser(User user) throws Exception {

        String sql = "UPDATE user SET full_name=?, email=?, phone=?, role=?, password=? WHERE user_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getPassword());
            ps.setInt(6, user.getUserId());

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int userId) throws Exception {
        String sql = "DELETE FROM user WHERE user_id=?";

        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
