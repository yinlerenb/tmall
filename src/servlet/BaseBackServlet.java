package servlet;

import dao.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.omg.PortableInterceptor.INACTIVE;
import util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BaseBackServlet")
public abstract class BaseBackServlet extends HttpServlet {

    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);

    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);

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
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                count = Integer.parseInt(req.getParameter("page.count"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            Page page = new Page(start, count);
            //借助反射调用对应的方法
            String method = (String) req.getAttribute("method");
            Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, req, resp, page).toString();
            //根据方法的返回值执行对应的操作
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

    public InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
        InputStream is = null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //设置上传文件的大小
        factory.setSizeThreshold(1024 * 10240);
        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    //获取上传文件的输入流
                    is = item.getInputStream();
                } else {
                    String paramName = item.getFieldName();
                    String paramValue = item.getString();
                    paramValue = new String(paramValue.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    params.put(paramName, paramValue);
                }
            }
        } catch (FileUploadException | IOException e) {
            e.printStackTrace();
        }
        return is;
    }
}
