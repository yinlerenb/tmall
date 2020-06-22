package filter;

import bean.User;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebFilter(filterName = "ForeAuthFilter")
public class ForeAuthFilter implements Filter {
    //注册登陆产品首页分类查询
    // 购买加入购物车查看购物车查看订单
    // 当访问的ur1是需要登陆才能完成的功能的时候
    // 过滤器会对当前session中的用户做校验
    // 如果发现用户未登录 则直接跳转到login.jsp
    // 如果用户已经登陆过滤器放行
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //获取项目名称/tmall
        String contextPath = request.getServletContext().getContextPath();
        //准备字符串数组 把不需要登陆也可以访问的路径存放在这个数组中
        String[] noNeedAuthPage = new String[]{
                "homepage",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"
        };
        String uri = request.getRequestURI(); // /tmall/xx
        uri = StringUtils.remove(uri, contextPath);
        if (uri.startsWith("/fore") && !uri.startsWith("/foreServlet")) {
            String method = StringUtils.substringAfterLast(uri, "/fore");
            //如果从uri中载取出来的方法名称并不在我们所提供的数组的字符串范围内
            // 如果发现uri对应的请求访问的是需要登陆的功能模块
            if (!Arrays.asList(noNeedAuthPage).contains(method)) {
                //从
                User user = (User) request.getSession().getAttribute("user");
                if (null == user) {//如果验证时发现用户不处于登陆状态
                    // 页面直接重定向到登陆页面
                    response.sendRedirect("login.jsp");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
