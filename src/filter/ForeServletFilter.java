package filter;

import bean.Category;
import bean.OrderItem;
import bean.User;
import dao.CategoryDAO;
import dao.OrderItemDAO;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "ForeServletFilter")
public class ForeServletFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //对req和res做类型转换
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String contextPath = request.getServletContext().getContextPath();
        //获取当前项目的url部分
        System.out.println("contextPath值为：" + contextPath);
        request.getServletContext().setAttribute("contextPath", contextPath);
        //从session中获取已经存储的用户信息
        User user = (User) request.getSession().getAttribute("user");
        //默认的初始化购物车数量
        int cartTotalItemNumber = 0;
        if (null != user) {
            //获取当前用户的所有订单项集合
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for (OrderItem oi : ois) {
                cartTotalItemNumber += oi.getNumber();
            }
        }
        //把订单项总数存入到request对象中
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
        //获取分类集合
        List<Category> cs = (List<Category>) request.getAttribute("cs");
        if (null == cs) {
            cs = new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        //如果uri以/fore开头并且不以/foreServlet开头
        if (uri.startsWith("/fore") && !uri.startsWith("/foreServlet")) {
            //去除method部分的register字符串
            String method = StringUtils.substringAfterLast(uri, " / fore ");
            request.setAttribute("method", method);
            //截取uri中的方法名称并且将方法名称传给转发之后的servlet
            req.getRequestDispatcher("/foreServlet").forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
