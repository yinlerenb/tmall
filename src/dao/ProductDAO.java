package dao;

import bean.Category;
import bean.Product;
import util.DBUtil;
import util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select count(*) from Product";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Product bean) {
        String sql = "insert into Product values(null,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.toString());
            ps.setFloat(3, bean.getOriginalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
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

    public void update(Product bean) {
        String sql = "update Product set name=?, subTitle=?, originalPrice=?, promotePrice=?, stock=?, cid=?, createDate=? where id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.toString());
            ps.setFloat(3, bean.getOriginalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(8, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "delete from Product where id=" + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product get(int id) {
        Product bean = new Product();
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select * from Product where id=" + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                bean = new Product();
                bean.setId(id);
                bean.setName(rs.getString(2));
                bean.setSubTitle(rs.getString(3));
                bean.setOriginalPrice(rs.getFloat(4));
                bean.setPromotePrice(rs.getFloat(5));
                bean.setStock(rs.getInt(6));
                bean.setCategory(new CategoryDAO().get(rs.getInt(7)));
                bean.setCreateDate(DateUtil.t2d(rs.getTimestamp(8)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<Product> list(int start, int count) {
        List<Product> beans = new ArrayList<>();
        String sql = "select * from Product order by id desc limit ?,?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product bean = new Product();
                bean.setId(rs.getInt(1));
                bean.setName(rs.getString(2));
                bean.setSubTitle(rs.getString(3));
                bean.setOriginalPrice(rs.getFloat(4));
                bean.setPromotePrice(rs.getFloat(5));
                bean.setStock(rs.getInt(6));
                bean.setCategory(new CategoryDAO().get(rs.getInt(7)));
                bean.setCreateDate(DateUtil.t2d(rs.getTimestamp(8)));
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<Product> list() {
        return list(0, Integer.MAX_VALUE);
    }

    public int getTotal(int cid){
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select count(*) from Product where cid="+cid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<Product> list(int cid) {
        List<Product> beans = new ArrayList<>();
        String sql = "select * from Product where cid=? order by id desc";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product bean = new Product();
                bean.setId(rs.getInt(1));
                bean.setName(rs.getString(2));
                bean.setSubTitle(rs.getString(3));
                bean.setOriginalPrice(rs.getFloat(4));
                bean.setPromotePrice(rs.getFloat(5));
                bean.setStock(rs.getInt(6));
                bean.setCategory(new CategoryDAO().get(cid));
                bean.setCreateDate(DateUtil.t2d(rs.getTimestamp(8)));
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

}
