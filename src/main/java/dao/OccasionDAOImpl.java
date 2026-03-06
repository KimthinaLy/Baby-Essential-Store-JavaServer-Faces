/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Occasion;
import util.DBUtil;

/**
 *
 * @author Admin
 */
public class OccasionDAOImpl implements OccasionDAO{
    @Override
    public List<Occasion> getAllOccasions() throws Exception {

        List<Occasion> list = new ArrayList<>();

        String sql = "SELECT * FROM occasions";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                Occasion o = new Occasion();

                o.setOccasionId(rs.getInt("occasion_id"));
                o.setOccasionName(rs.getString("occasion_name"));

                list.add(o);
            }
        }

        return list;
    }

    @Override
    public Occasion getOccasionById(int id) throws Exception {

        String sql = "SELECT * FROM occasions WHERE occasion_id=?";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                Occasion o = new Occasion();

                o.setOccasionId(rs.getInt("occasion_id"));
                o.setOccasionName(rs.getString("occasion_name"));

                return o;
            }
        }

        return null;
    }

    @Override
    public void insertOccasion(Occasion occasion) throws Exception {

        String sql = "INSERT INTO occasions(occasion_name) VALUES(?)";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, occasion.getOccasionName());

            ps.executeUpdate();
        }
    }

    @Override
    public void updateOccasion(Occasion occasion) throws Exception {

        String sql = "UPDATE occasions SET occasion_name=? WHERE occasion_id=?";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, occasion.getOccasionName());
            ps.setInt(2, occasion.getOccasionId());

            ps.executeUpdate();
        }
    }

    @Override
    public void deleteOccasion(int id) throws Exception {

        String sql = "DELETE FROM occasions WHERE occasion_id=?";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, id);

            ps.executeUpdate();
        }
    }
}
