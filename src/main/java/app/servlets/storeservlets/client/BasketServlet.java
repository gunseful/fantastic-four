package app.servlets.storeservlets.client;

import app.entities.user.User;
import app.model.controller.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasketServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //getting this session user to work with
        User user = (User) req.getSession().getAttribute("user");
        UserController controller = (UserController) req.getSession().getAttribute("controller");
        req.setAttribute("basket", controller.getBasketList(user));
        //если юзер не админ кидает на на вьюшку корзина
        try {
            if (!user.isAdministrator()) {
                logger.info("User=" + user.getNickname() + "requests his basket list");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/basket.jsp");
                requestDispatcher.forward(req, resp);
            } else {
                resp.sendRedirect("/listAdmin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //getting this session user to work with
        User user = (User) req.getSession().getAttribute("user");
        UserController controller = new UserController();
        //when we click on bottom "Заказа" we give parameter getOrder and then making some order by special methid in Model.class and redirect to -> orders
        try {
            if (req.getParameter("getOrder") != null) {
                controller.makeOrder(user);
                logger.info("User=" + user.getNickname() + " makes order");
                resp.sendRedirect("/orders");
            }
        } catch (Exception ignored) {
        }

        //we get some product for delete from the basket, taking products, checks is there in the db those product -> removing
        //if client choose nothing -> exception
        try {
            if (req.getParameterValues("productForDelete") != null) {
                String[] productsID = req.getParameterValues("productForDelete");
                for (String productID : productsID) {
                    logger.info("User=" + user.getNickname() + " delete product from his basket");
                    controller.deleteProductFromBasket(productID.trim());
                    doGet(req, resp);
                }
            }
        } catch (NullPointerException exception) {
            logger.error("User=" + user.getNickname() + " was failed");
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
    }
}

