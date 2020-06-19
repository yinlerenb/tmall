package servlet;

import bean.Category;
import bean.Property;
import util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PropertyServlet")
public class PropertyServlet extends BaseBackServlet {

    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        //获取你新增的属性名称值
        String name = request.getParameter("name");
        Property p = new Property();
        p.setCategory(c);
        p.setName(name);
        propertyDAO.add(p);
        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        propertyDAO.delete(id);
        return "@admin_property_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        request.setAttribute("p", p);
        return "admin/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        Property p = new Property();
        //设置 Property对象的属性
        p.setCategory(c);
        p.setId(id);
        p.setName(name);
        //调用DAO去做属性数据更新
        propertyDAO.update(p);
        //返回uri在父类中找到对应的页面跳转
        return "@admin_property_list?cid=" + p.getCategory().getId();
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        //取出 request请求中的cid参数值
        int cid = Integer.parseInt(request.getParameter("cid"));
        //通过cid获取分类对象
        Category c = categoryDAO.get(cid);
        //获取指定范围的数据集合
        List<Property> ps = propertyDAO.list(cid, page.getStart(), page.getCount());
        //获取指定分类下的属性总数
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid=" + c.getId());
        //将数据存入到 request请求中
        request.setAttribute("ps", ps);
        request.setAttribute("c", c);
        request.setAttribute("page", page);
        return "admin/listProperty.jsp";
    }
}
