package app.controller.servlets.storeservlets.client;

import app.controller.service.OrderServiceImpl;
import app.controller.service.ProductServiceImpl;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListClientServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //получаем юзера
        User user = (User) req.getSession().getAttribute("user");
        ProductServiceImpl productService = new ProductServiceImpl();
        //получаем лист товаров
        //проверяем, если админ сюда зайдет его кинет на страницу админа, если обычный клиент, открывает вьюшку
        try {
            req.setAttribute("products", productService.getList());
            if (!user.isAdministrator()) {
                logger.info("User=" + user.getNickname() + " has requested product list");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/listClient.jsp");
                requestDispatcher.forward(req, resp);
            } else {
                resp.sendRedirect("/listAdmin");
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        OrderServiceImpl orderService = new OrderServiceImpl();
        //выбираем в списке чо хотим купить, то есть добавить в корзину и кликаем - > Летит в корзину
        try {
            if (req.getParameterValues("productForBuy") != null) {
                for (String productId : req.getParameterValues("productForBuy")) {
                    if (!orderService.updateBasket(true, user, Integer.parseInt(productId.trim()))) {
                        orderService.addToBasket(user, Integer.parseInt(productId.trim()));
                    }
                }
            } else {
                logger.info("User=" + user.getNickname() + " chose nothing");
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }
        } catch (NullPointerException e) {
            logger.error("User=" + user.getNickname() + " chose nothing", e);
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        //ну прост обновляем вьюшку
        doGet(req, resp);
    }
}
