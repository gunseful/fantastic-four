package app.servlets.storeservlets;

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

public class OrdersServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            if (req.getAttribute("Orders") != null) {
                resp.sendRedirect("/orders");
            }
        } catch (Exception ignored) {
        }

        User user = (User) req.getSession().getAttribute("user");


        UserController controller = (UserController) req.getSession().getAttribute("controller");
        req.setAttribute("isInBlackList", controller.checkBlackList(user));
        req.setAttribute("orders", controller.getOrders2(user));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/common/orders.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("user");
        UserController controller = (UserController)req.getSession().getAttribute("controller");
        //если нажимается кнопка Pay и был выбран хоть один Заказ, то оплачивается
        try {
            if (req.getParameter("Pay") != null && req.getParameterValues("orders") != null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    logger.info("User=" + user.getNickname() + " has payed order");
                    System.out.println("TRY TO PAY " +orderID);
                    controller.payOrder2((Integer.parseInt((orderID.trim()))));
//                    Model.getInstance().payOrder(Integer.parseInt(orderID));
                }
            }else if(req.getParameterValues("orders")!=null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    logger.info("User=" + user.getNickname() + " has deleted order "+orderID);
                    controller.deleteOrder(Integer.parseInt(orderID.trim()));
                }
            }else{
                if (req.getParameter("block") != null) {
                    User blackUser = controller.getUser(Integer.parseInt(req.getParameter("block")));
                    logger.error("User=" + blackUser.getNickname() + " has been banned");
                    controller.addUserToBlackList(blackUser);
                }else{
                    req.setAttribute("nullData", "");
                    doGet(req, resp);
                }
            }
        } catch (NullPointerException ignored) {
        }
        doGet(req, resp);
    }
}
