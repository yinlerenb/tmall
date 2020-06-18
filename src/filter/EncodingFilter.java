package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "EncodingFilter")
public class EncodingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response=(HttpServletResponse) resp;
        HttpServletRequest request=(HttpServletRequest) req;
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request,response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
