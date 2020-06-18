package dao;

import java.util.ArrayList;
import java.util.Date;

import bean.Review;
import util.DBUtil;
import util.DateUtil;

import java.sql.*;
import java.util.List;

public class ReviewDAO {
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from Review";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    public void add(Review bean) {
        String sql = "insert into Review values(null,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Review bean) {
        String sql = "update Review set content=? ,uid=? ,pid=?, datetime=? where id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(5, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "delete from Review where id=" + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Review get(int id) {
        Review bean = null;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select * from Review where id=" + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                bean = new Review();
                String content = rs.getString(2);
                int uid = rs.getInt(3);
                int pid = rs.getInt(4);
                Date date = DateUtil.t2d(rs.getTimestamp(5));
                bean.setContent(content);
                bean.setUser(new UserDAO().get(uid));
                bean.setProduct(new ProductDAO().get(pid));
                bean.setCreateDate(date);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public int getCount(int pid) {
        int count = 0;
        String sql = "select count(*) from Review where pid=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Review> list(int pid, int start, int count) {
        List<Review> beans = new ArrayList<>();
        String sql = "select * from Category order where pid=? by id desc limit ?,?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,pid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review bean = new Review();
                int id=rs.getInt(1);
                String content = rs.getString(2);
                int uid = rs.getInt(3);
                Date date = DateUtil.t2d(rs.getTimestamp(5));
                bean.setContent(content);
                bean.setUser(new UserDAO().get(uid));
                bean.setProduct(new ProductDAO().get(pid));
                bean.setCreateDate(date);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<Review> list(int pid) {
        return list(pid, 0, Short.MAX_VALUE);
    }

    public boolean isExist(String content, int pid) {
        String sql = "select * from Review where content = ? and pid = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, pid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
