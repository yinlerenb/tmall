package servlet;

import bean.*;
import comparator.*;
import dao.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;
import util.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ForeServlet")
public class ForeServlet extends BaseForeServlet {
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs = new CategoryDAO().list();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        request.setAttribute("cs", cs);
        return "home.jsp";
    }

    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        //读取传递过来的form表单中用户所输入的信息
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        //防止代码注入
        // <script>alert(111)</script>
        name = HtmlUtils.htmlEscape(name);
        System.out.println(name);
        //判断注册的用户是否存在
        boolean exist = userDAO.isExist(name);
        if (exist) {
            request.setAttribute("msg", "用户名已经被使用，不能使用");
            return "register.jsp";
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        userDAO.add(user);
        return "@registerSuccess.jsp";
    }

    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        //获取用户名
        String name = request.getParameter(" name ");
        //格式处理 防止sq1注入
        name = HtmlUtils.htmlEscape(name);
        //获取密码
        String password = request.getParameter(" password ");
        //获取用户对象
        User user = userDAO.get(name, password);
        //如果用户不存在
        if (null == user) {
            request.setAttribute("msg", "账号密码错误");
            return "login.jsp";
        }
        //把该用户所对应的实体类对象存入到session中
        request.getSession().setAttribute("user", user);
        //会重定向到ur1为/forehome的路径
        // 因为是重定向浏览器会再次向服务器发送一次request请求
        // 该请求也会再次被过滤器过滤
        return "@forehome";
    }

    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        //获取请求中携带的参数147
        int pid = Integer.parseInt(request.getParameter("pid"));
        //获取对应pia的Product对象
        Product p = productDAO.get(pid);
        //获取的是产品的单个图片集合
        List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
        //获取产品详情结合
        List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
        //将获取到的图片集合存入到对象p中
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);
        //获取产品所有的属性值
        List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
        //该商品对应的所有评价
        List<Review> reviews = reviewDAO.list(p.getId());
        //设置产品的销量和评价数量
        productDAO.setSaleAndReviewNumber(p);
        //吧上述取到的所有数据存入到request请求中
        request.setAttribute("reviews", reviews);
        request.setAttribute("p", p);
        request.setAttribute("pvs", pvs);
        return "product.jsp";
    }

    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        //删除session中的数据
        request.getSession().removeAttribute("user");
        //返回首页home.jsp
        return "@forehome";
    }

    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null != user)
            return "%success";
        return "%fail";
    }

    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
        //从ajax发送的请求中获取你需要的data
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = userDAO.get(name, password);
        if (null == user) {
            return "%fail";
        }
        request.getSession().setAttribute("user", user);
        return "%success";
    }

    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
        //获取参数中的分类id cid
        int cid = Integer.parseInt(request.getParameter("cid"));
        //根据cid获取对应的分类对象
        Category c = new CategoryDAO().get(cid);
        new ProductDAO().fill(c);
        new ProductDAO().setSaleAndReviewNumber(c.getProducts());
        //获取排序方式
        String sort = request.getParameter("sort");
        if (null != sort) {
            switch (sort) {
                case "review":
                    c.getProducts().sort(new ProductReviewComparator());
                    break;
                case "date":
                    c.getProducts().sort(new ProductDateComparator());
                    break;
                case "saleCount":
                    c.getProducts().sort(new ProductSaleCountComparator());
                    break;
                case "price":
                    c.getProducts().sort(new ProductPriceComparator());
                    break;
                case "all":
                    c.getProducts().sort(new ProductAllComparator());
                    break;
            }
        }
        //将已经排序好的商品
        request.setAttribute("a", c);
        return "category.jsp";
    }

    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
        String keyword = request.getParameter("keyword");
        List<Product> ps = new ProductDAO().search(keyword, 0, 20);
        productDAO.setSaleAndReviewNumber(ps);
        request.setAttribute("ps", ps);
        return "searchResult.jsp";
    }

    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter(" pid "));
        int num = Integer.parseInt(request.getParameter(" num "));
        Product p = productDAO.get(pid);
        int oiid = 0;
        User user = (User) request.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == p.getId()) {
                oi.setNumber(oi.getNumber() + num);
                orderItemDAO.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }
        if (!found) {
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setNumber(num);
            oi.setProduct(p);
            orderItemDAO.add(oi);
            oiid = oi.getId();
        }
        return "@forebuy?oiid=" + oiid;
    }

    public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
        String[] oiids = request.getParameterValues("oiid");
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;
        for (String strid : oiids) {
            int oiid = Integer.parseInt(strid);
            OrderItem oi = orderItemDAO.get(oiid);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            ois.add(oi);
        }
        request.getSession().setAttribute("ois", ois);
        request.setAttribute("total", total);
        return "buy.jsp";
    }

    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        int num = Integer.parseInt(request.getParameter("num"));
        User user = (User) request.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == p.getId()) {
                oi.setNumber(oi.getNumber() + num);
                orderItemDAO.update(oi);
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setNumber(num);
            oi.setProduct(p);
            orderItemDAO.add(oi);
        }
        return "%success";
    }

    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        request.setAttribute("ois", ois);
        return "cart.jsp";
    }

    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null == user)
            return "%fail";
        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("number"));
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == pid) {
                oi.setNumber(number);
                orderItemDAO.update(oi);
                break;
            }
        }
        return "%success";
    }

    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null == user)
            return "%fail";
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        orderItemDAO.delete(oiid);
        return "%success";
    }

    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");

        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");

        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderDAO.waitPay);
        orderDAO.add(order);
        List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
        float total = 0;
        for (OrderItem oi : ois) {
            oi.setOrder(order);
            orderItemDAO.update(oi);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
        }
        return "@forealipay?oid=" + order.getId() + "&total=" + total;
    }

    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page) {
        return "alipay. jsp";
    }

    public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitDelivery);
        order.setPayDate(new Date());
        new OrderDAO().update(order);
        request.setAttribute("o", order);
        return "payed.jsp";
    }

    public String bought(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<Order> os = orderDAO.list(user.getId(), OrderDAO.delete);
        orderItemDAO.fill(os);
        request.setAttribute("os", os);
        return "bought.jsp";
    }

    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        request.setAttribute("o", o);
        return "confirmPay.jsp";
    }

    public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.waitReview);
        o.setConfirmDate(new Date());
        orderDAO.update(o);
        return "orderConfirmed.jsp";
    }

    public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.delete);
        orderDAO.update(o);
        return "%success";
    }

    public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewDAO.list(p.getId());
        productDAO.setSaleAndReviewNumber(p);
        request.setAttribute("p", p);
        request.setAttribute("o", o);
        request.setAttribute("reviews", reviews);
        return "review.jsp";
    }

    public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.finish);
        orderDAO.update(o);
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        String content = request.getParameter("content");
        content = HtmlUtils.htmlEscape(content);
        User user = (User) request.getSession().getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewDAO.add(review);
        return "@forereview?oid=" + oid + "&showonly=true";
    }
}
