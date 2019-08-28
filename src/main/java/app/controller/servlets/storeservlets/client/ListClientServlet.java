package app.controller.servlets.storeservlets.client;

import app.model.products.Product;
import app.model.user.User;
import app.controller.service.OrderServiceImpl;
import app.controller.service.ProductServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

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
        ProductServiceImpl productService = new ProductServiceImpl();
        OrderServiceImpl orderService = new OrderServiceImpl();
        //выбираем в списке чо хотим купить, то есть добавить в корзину и кликаем - > Летит в корзину
        try {
            if (req.getParameterValues("productForBuy") != null) {
                String[] productsID = req.getParameterValues("productForBuy");
                //опять юзера из сессии достаем
                for (String productID : productsID) {
                    for (Product product : productService.getList()) {
                        //проверяем есть ли ваще продукт в базе данных
                        if (String.valueOf(product.getId()).equals(productID.trim())) {
                            logger.info("User=" + user.getNickname() + " has added product to his basket");
//                            //если есть добавляет в базу данных
                            if(!orderService.updateBasket(true,user,product.getId())){
                                orderService.addToBasket(user,product.getId());
                            }
                        }
                    }
                }
            } else {
                logger.error("User=" + user.getNickname() + " made a mistake");
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }
        } catch (NullPointerException | SQLException e) {
            logger.error("User=" + user.getNickname() + " made a mistake");
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        //ну прост обновляем вьюшку
        doGet(req, resp);
    }
}
