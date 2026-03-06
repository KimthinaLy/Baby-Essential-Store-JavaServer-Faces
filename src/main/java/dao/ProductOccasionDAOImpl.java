/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class ProductOccasionDAOImpl implements ProductOccasionDAO{
     @Override
    public void insertProductOccasion(int productId, int occasionId) throws Exception {

        String sql = "INSERT INTO product_occasion(product_id, occasion_id) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, occasionId);
            ps.executeUpdate();
        }
    }

    @Override
    public void insertProductOccasion(int productId, List<Integer> occasionIds) throws Exception {

        String sql = "INSERT INTO product_occasion(product_id, occasion_id) VALUES (?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (Integer occId : occasionIds) {
                ps.setInt(1, productId);
                ps.setInt(2, occId);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    @Override
    public List<Integer> getOccasionIdsByProductId(int productId) throws Exception {

        List<Integer> occasionIds = new ArrayList<>();

        String sql = "SELECT occasion_id FROM product_occasion WHERE product_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                occasionIds.add(rs.getInt("occasion_id"));
            }
        }

        return occasionIds;
    }

    @Override
    public void deleteProductOccasion(int productId, int occasionId) throws Exception {

        String sql = "DELETE FROM product_occasion WHERE product_id=? AND occasion_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, occasionId);

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteAllOccasionsByProductId(int productId) throws Exception {

        String sql = "DELETE FROM product_occasion WHERE product_id=?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateProductOccasions(int productId, List<Integer> newOccasionIds) throws Exception {

        Connection con = null;

        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            String deleteSQL = "DELETE FROM product_occasion WHERE product_id=?";
            try (PreparedStatement ps = con.prepareStatement(deleteSQL)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            String insertSQL = "INSERT INTO product_occasion(product_id, occasion_id) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertSQL)) {

                for (Integer occId : newOccasionIds) {
                    ps.setInt(1, productId);
                    ps.setInt(2, occId);
                    ps.addBatch();
                }

                ps.executeBatch();
            }

            con.commit();

        } catch (Exception e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) con.close();
        }
    }
}
