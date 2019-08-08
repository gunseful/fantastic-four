package app.servlets.storeservlets;

import app.entities.user.User;
import app.model.Model;

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


        req.setAttribute("orders", Model.getInstance().getOrders(user));
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/common/orders.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Orders servlet doPost");
        //если нажимается кнопка Pay и был выбран хоть один Заказ, то оплачивается
        try {
            if (req.getParameter("Pay") != null && req.getParameterValues("orders") != null) {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    Model.getInstance().payOrder(Integer.parseInt(orderID));
                }
            }
            //если падает эксепшен и нет пея, но есть были выбраны ордеры - удаляем их
        } catch (NullPointerException exception) {
            try {
                String[] ordersID = req.getParameterValues("orders");
                for (String orderID : ordersID) {
                    Model.getInstance().deleteOrder(Integer.parseInt(orderID.trim()));
                }
                //если еще один эксепшн, типа значит ничего не выбиралось, значит нажалась кнопка блок
                //вызывается метод из Модели который добавляет юзера в черный список по айдишнику
            } catch (Exception e) {
                try {
                    if (req.getParameter("block") != null) {
                        Model.getInstance().addUserToBlackList(Model.getInstance().getUser(Integer.parseInt(req.getParameter("block"))));
                    }
                    //это ласт эксепшн, значит ничего ваще не выбиралось и вот значит нулдата и выскакивает ошибка
                } catch (Exception exept) {
                    req.setAttribute("nullData", "");
                    doGet(req, resp);
                }
            }
        }
        doGet(req, resp);
    }
}
