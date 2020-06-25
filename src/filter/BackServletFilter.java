package filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "BackServletFilter")
public class BackServletFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //req,resp类型转换
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //获取项目根路径
        String contextPath = request.getServletContext().getContextPath();
        //获取uri的最后部分
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        //判断是否为后台请求
        if (uri.startsWith("/admin_")) {
            //获取对应的servlet名称
            String servletPath = StringUtils.substringBetween(uri, "_", "_") + "Servlet";
            //获取对应的方法
            String method = StringUtils.substringAfterLast(uri, "_");
            //将方法设为属性传递
            request.setAttribute("method", method);
            //转发到对应的servlet
            request.getRequestDispatcher("/" + servletPath).forward(request, response);
            return;
        }
        //请求放行
        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
