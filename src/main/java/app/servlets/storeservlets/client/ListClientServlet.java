package app.servlets.storeservlets.client;

import app.entities.products.Product;
import app.entities.user.User;
import app.model.daoFake.add.AddToBasket;
import app.model.daoFake.get.GetList;
import app.model.daoFake.update.UpdateBasket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ListClientServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //получаем юзера
        User user = (User) req.getSession().getAttribute("user");
//        Repository controller = (Repository) req.getSession().getAttribute("controller");
        //получаем лист товаров
        req.setAttribute("products", new GetList().start());
        //проверяем, если админ сюда зайдет его кинет на страницу админа, если обычный клиент, открывает вьюшку
        try {
            if (!user.isAdministrator()) {
                logger.info("User=" + user.getNickname() + " has requested product list");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/listClient.jsp");
                requestDispatcher.forward(req, resp);
            } else {
                resp.sendRedirect("/listAdmin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        Repository controller = (Repository) req.getSession().getAttribute("controller");
        User user = (User) req.getSession().getAttribute("user");
        //выбираем в списке чо хотим купить, то есть добавить в корзину и кликаем - > Летит в корзину
        try {
            if (req.getParameterValues("productForBuy") != null) {
                String[] productsID = req.getParameterValues("productForBuy");
                //опять юзера из сессии достаем
                for (String productID : productsID) {
                    for (Product product : (List<Product>) new GetList().start()) {
                        //проверяем есть ли ваще продукт в базе данных
                        if (String.valueOf(product.getId()).equals(productID.trim())) {
                            logger.info("User=" + user.getNickname() + " has added product to his basket");
//                            //если есть добавляет в базу данных
                            if(!(boolean) new UpdateBasket(true, user,product.getId()).start()){
                                new AddToBasket(user,product.getId()).start();
//                                controller.addToBasket(user,product.getId());
                            }
                        }
                    }
                }
            } else {
                logger.error("User=" + user.getNickname() + " made a mistake");
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }
        } catch (NullPointerException e) {
            logger.error("User=" + user.getNickname() + " made a mistake");
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        //ну прост обновляем вьюшку
        doGet(req, resp);
    }
}
