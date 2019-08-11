package app.servlets.storeservlets;

import app.entities.user.User;
import app.model.controller.AbstractController;
import app.model.controller.UserController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Orders servlet doGet");

        try {
            if (req.getAttribute("Orders") != null) {
                resp.sendRedirect("/orders");
            }
        } catch (Exception ignored) {
        }

        User user = (User) req.getSession().getAttribute("user");


        AbstractController controller = (UserController) req.getSession().getAttribute("controller");
        req.setAttribute("isInBlackList", controller.checkBlackList(user));
        req.setAttribute("orders", controller.getOrders(user));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/common/orders.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Orders servlet doPost");
        AbstractController controller = (UserController)req.getSession().getAttribute("controller");
        //если нажимается кнопка Pay и был выбран хоть один Заказ, то оплачивается
        try {
            if (req.getParameter("Pay") != null && req.getParameterValues("orders") != null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    controller.payOrder((Integer.parseInt((orderID))));
//                    Model.getInstance().payOrder(Integer.parseInt(orderID));
                }
            }else if(req.getParameterValues("orders")!=null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    controller.deleteOrder(Integer.parseInt(orderID.trim()));
                }
            }else{
                if (req.getParameter("block") != null) {
                    User user = controller.getUser(Integer.parseInt(req.getParameter("block")));
                    controller.addUserToBlackList(user);
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
