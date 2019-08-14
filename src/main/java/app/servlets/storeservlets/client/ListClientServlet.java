package app.servlets.storeservlets.client;

import app.entities.products.Product;
import app.entities.user.User;
import app.model.controller.AbstractController;
import app.model.controller.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListClientServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //получаем юзера
        User user = (User)req.getSession().getAttribute("user");
        AbstractController controller = (UserController) req.getSession().getAttribute("controller");

        //получаем лист товаров
        req.setAttribute("products", controller.getList());
        //проверяем, если админ сюда зайдет его кинет на страницу админа, если обычный клиент, открывает вьюшку
        try {
            if(!user.isAdministrator()) {
                logger.info("User=" + user.getNickname() + " has requested product list");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/listClient.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                resp.sendRedirect("/listAdmin");}
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserController controller = (UserController) req.getSession().getAttribute("controller");
        User user = (User)req.getSession().getAttribute("user");
        //выбираем в списке чо хотим купить, то есть добавить в корзину и кликаем - > Летит в корзину
        try {
            if (req.getParameterValues("productForBuy") != null) {
                String[] productsID = req.getParameterValues("productForBuy");
                //опять юзера из сессии достаем

                for (String productID : productsID) {
                    System.out.println(productID.trim());
                    for(Product product : controller.getList()){
                        //проверяем есть ли ваще продукт в базе данных
                        if(String.valueOf(product.getId()).equals(productID.trim())){
                            logger.info("User=" + user.getNickname() + " has added product to his basket");
//                            user.getBasket().getList().add(product);
//                            //если есть добавляет в базу данных
                            controller.addToBasketS(user, product.getId());
                            }
                    }
                }
            }else{
                logger.error("User=" + user.getNickname() + " made a mistake");
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }
        }catch (NullPointerException e){
            logger.error("User=" + user.getNickname() + " made a mistake");
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        //ну прост обновляем вьюшку
        doGet(req, resp);
    }
}
