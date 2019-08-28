package app.controller.servlets.storeservlets.client;

import app.model.user.User;
import app.controller.service.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class BasketServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting this session user to work with
        User user = (User) req.getSession().getAttribute("user");
        OrderServiceImpl orderService = new OrderServiceImpl();
        //если юзер не админ кидает на на вьюшку корзина
        try {
            req.setAttribute("basket", orderService.getBasket(user));
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        //getting this session user to work with
        User user = (User) req.getSession().getAttribute("user");
        OrderServiceImpl orderService = new OrderServiceImpl();

        try {
            if (req.getParameter("plus") != null) {
                int i = Integer.parseInt(req.getParameter("plus"));
                orderService.updateBasket(true,user,i);
                doGet(req, resp);
            }
        } catch (Exception e) {
            logger.error(e);
        }

        try {
            if (req.getParameter("minus") != null) {
                int i = Integer.parseInt(req.getParameter("minus"));
                orderService.updateBasket(false,user,i);
                doGet(req, resp);
            }
        } catch (Exception e) {
            logger.error(e);
        }


        //when we click on bottom "Заказа" we give parameter getOrder and then making some order by special methid in Model.class and redirect to -> orders
        try {
            if (req.getParameter("getOrder") != null) {
                orderService.makeOrder(user);
                logger.info("User=" + user.getNickname() + " makes order");
                resp.sendRedirect("/orders");
            }
        } catch (Exception e) {
            logger.error(e);
        }
        //we get some product for delete from the basket, taking products, checks is there in the db those product -> removing
        //if client choose nothing -> exception
        try {
            if (req.getParameterValues("productForDelete") != null) {
                String[] productsID = req.getParameterValues("productForDelete");
                for (String productID : productsID) {
                    logger.info("User=" + user.getNickname() + " delete product from his basket");
                    orderService.deleteProductFromBasket(user,Integer.parseInt(productID.trim()));
                    doGet(req, resp);
                }
            }else{
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }
        } catch (NullPointerException | SQLException exception) {
            logger.error("User=" + user.getNickname() + " was failed");
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
    }
}

