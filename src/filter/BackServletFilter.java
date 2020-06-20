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
        HttpServletResponse response=(HttpServletResponse) resp;
        HttpServletRequest request=(HttpServletRequest) req;
        String contextPath=request.getServletContext().getContextPath();
        //截取url的最后部分
        String uri=request.getRequestURI();
        uri= StringUtils.remove(uri,contextPath);
        if (uri.startsWith("/admin_")){
            String servletPath =StringUtils.substringBetween(uri,"_","_")+"Servlet";
            String method=StringUtils.substringAfterLast(uri,"_");
            request.setAttribute("method",method);
            return;
        }
        //请求放行
        chain.doFilter(request,response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
