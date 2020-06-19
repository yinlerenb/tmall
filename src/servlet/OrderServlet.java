package servlet;

import bean.Order;
import dao.OrderDAO;
import util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "OrderServlet")
public class OrderServlet extends BaseBackServlet {

    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Order o = orderDAO.get(id);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderDAO.waitConfirm);
        orderDAO.update(o);
        return "@admin_order_list";
    }

    public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Order o = orderDAO.get(id);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderDAO.waitConfirm);
        orderDAO.update(o);
        return "@admin_order_list";
    }
}
