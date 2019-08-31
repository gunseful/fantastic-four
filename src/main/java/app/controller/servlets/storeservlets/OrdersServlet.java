package app.controller.servlets.storeservlets;

import app.controller.service.OrderServiceImpl;
import app.controller.service.UserServiceImpl;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrdersServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            if (req.getAttribute("Orders") != null) {
                resp.sendRedirect("/orders");
            }

            User user = (User) req.getSession().getAttribute("user");

            UserServiceImpl userService = new UserServiceImpl();
            OrderServiceImpl orderService = new OrderServiceImpl();
            req.setAttribute("isInBlackList", userService.checkBlackList(user));
            req.setAttribute("orders", orderService.getOrders(user));

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/common/orders.jsp");
            requestDispatcher.forward(req, resp);

        } catch (Exception e) {
            logger.error(e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        OrderServiceImpl orderService = new OrderServiceImpl();
        UserServiceImpl userService = new UserServiceImpl();
        //если нажимается кнопка Pay и был выбран хоть один Заказ, то оплачивается
        try {
            if (req.getParameter("Pay") != null && req.getParameterValues("orders") != null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    System.out.println(orderID);
                    logger.info("User=" + user.getNickname() + " has payed order");
                    orderService.payOrder(Integer.parseInt(orderID));
                }
            } else if (req.getParameterValues("orders") != null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    logger.info("User=" + user.getNickname() + " has deleted order " + orderID);
                    orderService.deleteOrder(Integer.parseInt(orderID.trim()));
                }
            } else {
                if (req.getParameter("block") != null) {



                    userService.addToBlackList(Integer.parseInt(req.getParameter("block")));
                    logger.error("User= "+Integer.parseInt(req.getParameter("block"))+" has been banned");

                } else {
                    req.setAttribute("nullData", "");
                    doGet(req, resp);
                }
            }
        } catch (NullPointerException ignored) {
        }
        doGet(req, resp);
    }
}
