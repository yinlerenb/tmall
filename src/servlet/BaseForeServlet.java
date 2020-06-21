package servlet;

import dao.*;
import util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseForeServlet")
public class BaseForeServlet extends HttpServlet {
    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //分页
            int start = 0;
            int count = 5;
            try {
                start = Integer.parseInt(req.getParameter("page.start"));
                count = Integer.parseInt(req.getParameter("page.count"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            Page page = new Page(start, count);

            //借助反射调用对应的方法
            String method = (String) req.getAttribute("method");
            Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, req, resp, page).toString();

            //根据方法的返回值
            if (redirect.startsWith("@")) {
                resp.sendRedirect(redirect.substring(1));
            } else if (redirect.startsWith("%")) {
                resp.getWriter().print(redirect.substring(1));
            } else
                req.getRequestDispatcher(redirect).forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
