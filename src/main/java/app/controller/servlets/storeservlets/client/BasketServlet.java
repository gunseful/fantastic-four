package app.controller.servlets.storeservlets.client;

import app.controller.servlets.AbstractServlet;
import app.model.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

public class BasketServlet extends AbstractServlet {

    private enum ActionType {
        PLUS("plus"),
        MINUS("minus"),
        GET_ORDER("getOrder"),
        DELETE_PRODUCT("productForDelete");

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
            throw new NoSuchElementException();
        }
    }

    private final Map<ActionType, BiConsumer<HttpServletRequest, HttpServletResponse>> actionsMap = Map.of(
            ActionType.PLUS, plus(),
            ActionType.MINUS, minus(),
            ActionType.GET_ORDER, getOrder(),
            ActionType.DELETE_PRODUCT, deleteProduct()
    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting this session user to work with
        User user = user(req);
        //getting service to work with database
        try {
            //adding current user's basket(List<Product>) to request attribute as "basket"
            req.setAttribute("basket", orderService.getBasketProducts(user));
            //forward to basket page
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/basket.jsp");
            requestDispatcher.forward(req, resp);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        actionsMap.get(ActionType.actionType(req)).accept(req, resp);
        doGet(req, resp);
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> deleteProduct() {
        return (req, resp) -> {
            final var user = user(req);
            String[] productsId = req.getParameterValues(ActionType.DELETE_PRODUCT.value);
            for (String productId : productsId) {
                orderService.deleteProductFromBasket(user, Integer.parseInt(productId.trim()));
                logger.info("User=" + user.getNickname() + " delete product from his basket");
            }
        };
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> getOrder() {
        return (req, resp) -> {
            try {
                final var user = user(req);
                orderService.makeOrder(user);
                logger.info("User=" + user.getNickname() + " makes order");
                resp.sendRedirect("/orders");
            } catch (IOException e) {
                logger.error(e);
                throw new RuntimeException(e);
            }
        };
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> plus() {
        return (req, resp) ->
                orderService.increaseCount(user(req), Integer.parseInt(req.getParameter(ActionType.PLUS.value)));
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> minus() {
        return (req, resp) ->
                orderService.decreaseCount(user(req), Integer.parseInt(req.getParameter(ActionType.MINUS.value)));
    }
}

