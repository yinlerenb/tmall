package dao;

import bean.Order;
import util.DBUtil;
import util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select count(*) from Order_";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Order bean) {
        String sql = "insert into Order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getOrderCode());
            ps.setString(2, bean.getAddress());
            ps.setString(3, bean.getPost());
            ps.setString(4, bean.getReceiver());
            ps.setString(5, bean.getMobile());
            ps.setString(6, bean.getUserMessage());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());
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

    public void update(Order bean) {
        String sql = "update Order set orderCode=?, address=?, post=?, receiver=?, mobile=?, userMessage=?, createDate=?, payDate=?, deliveryDate=?, confirmDate=?, uid=?, status=? where id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, bean.getOrderCode());
            ps.setString(2, bean.getAddress());
            ps.setString(3, bean.getPost());
            ps.setString(4, bean.getReceiver());
            ps.setString(5, bean.getMobile());
            ps.setString(6, bean.getUserMessage());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());
            ps.setInt(13, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "delete from Order where id=" + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order get(int id) {
        Order bean = null;
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement()) {
            String sql = "select * from Order where id=" + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                bean = new Order();
                bean.setOrderCode(rs.getString(2));
                bean.setAddress(rs.getString(3));
                bean.setPost(rs.getString(4));
                bean.setReceiver(rs.getString(5));
                bean.setMobile(rs.getString(6));
                bean.setUserMessage(rs.getString(7));
                bean.setCreateDate(DateUtil.t2d(rs.getTimestamp(8)));
                bean.setPayDate(DateUtil.t2d(rs.getTimestamp(9)));
                bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp(10)));
                bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp(11)));
                bean.setUser(new UserDAO().get(rs.getInt(12)));
                bean.setStatus(rs.getString(13));
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<Order> list(int start, int count) {
        List<Order> beans = new ArrayList<>();
        String sql = "select * from Order_ order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, start);
            ps.setInt(2, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order bean = new Order();
                bean.setId(rs.getInt(1));
                bean.setOrderCode(rs.getString(2));
                bean.setAddress(rs.getString(3));
                bean.setPost(rs.getString(4));
                bean.setReceiver(rs.getString(5));
                bean.setMobile(rs.getString(6));
                bean.setUserMessage(rs.getString(7));
                bean.setCreateDate(DateUtil.t2d(rs.getTimestamp(8)));
                bean.setPayDate(DateUtil.t2d(rs.getTimestamp(9)));
                bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp(10)));
                bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp(11)));
                bean.setUser(new UserDAO().get(rs.getInt(12)));
                bean.setStatus(rs.getString(13));
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<Order> list() {
        return list(0, Short.MAX_VALUE);
    }
}
