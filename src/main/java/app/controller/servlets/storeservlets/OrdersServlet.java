package app.controller.servlets.storeservlets;

import app.controller.service.OrderServiceImpl;
import app.controller.service.UserServiceImpl;
import app.controller.servlets.AbstractServlet;
import app.model.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrdersServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting current user and Service to work with database
        User user = (User) req.getSession().getAttribute("user");
        UserServiceImpl userService = new UserServiceImpl();
        OrderServiceImpl orderService = new OrderServiceImpl();

        try {
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
        final var user = user(req);
        try {
            if (req.getParameter("block") != null) {
                userService.addToBlackList(Integer.parseInt(req.getParameter("block")));
                logger.error("User= "+Integer.parseInt(req.getParameter("block"))+" has been banned");

            } else {
                //second check is there "pay" parameter and orders, if yes - pay order, if no - next step
                if (req.getParameter("Pay") != null && req.getParameterValues("orders") != null) {
                    //getting orders ID as an array
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    orderService.payOrder(Integer.parseInt(orderID));
                    logger.info("User=" + user.getNickname() + " has payed order");
                }
                    //third check if there is no pay, but parameters "orders" exists - delete orders
                } else if (req.getParameterValues("orders") != null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    orderService.deleteOrder(Integer.parseInt(orderID.trim()));
                    logger.info("User=" + user.getNickname() + " has deleted order " + orderID);
                }
            } else {
                    req.setAttribute("nullData", "");
                }
            }
        } catch (NullPointerException e) {
            logger.error(e);
        }
        doGet(req, resp);
    }
}
