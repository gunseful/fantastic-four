package app.controller.servlets.storeservlets;

import app.controller.servlets.AbstractServlet;
import app.model.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

public class OrdersServlet extends AbstractServlet {

    private enum ActionType {
        ORDERS("orders"),
        BLOCK_USER("block");

        private String value;
        ActionType(String actionType) {
            value = actionType;
        }

        private static ActionType actionType(HttpServletRequest req) {
            for (ActionType value : values()) {
                if (req.getParameter(value.value) != null) {
                    return value;
                }
            }
            req.setAttribute("nullData", "nullData");
            return null;
        }
    }

    private final Map<ActionType, BiConsumer<HttpServletRequest, HttpServletResponse>> actionsMap = Map.of(
            ActionType.ORDERS, orders(),
            ActionType.BLOCK_USER, block()
    );


    private BiConsumer<HttpServletRequest, HttpServletResponse> block() {
        return (req, resp) ->
                userService.addToBlackList(Integer.parseInt(req.getParameter(ActionType.BLOCK_USER.value)));
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> orders() {
        return (req, resp) -> {
            var orders = req.getParameterValues(ActionType.ORDERS.value);
            if(req.getParameter("Pay")!=null) {
                Arrays.stream(orders).forEach(p -> orderService.payOrder(Integer.parseInt(p)));
            }else{
                Arrays.stream(orders).forEach(p -> orderService.deleteOrder(Integer.parseInt(p)));}
        };
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting current user and Service to work with database
        User user = (User) req.getSession().getAttribute("user");
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
        var act = ActionType.actionType(req);
        if (act != null) {
            var map = actionsMap.get(act);
            map.accept(req, resp);
        }
        doGet(req, resp);
    }
}
