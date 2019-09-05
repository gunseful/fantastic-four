package app.controller.servlets.storeservlets.client;

import app.controller.service.OrderService;
import app.controller.service.OrderServiceImpl;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;

public class BasketServlet extends HttpServlet {

    public static Logger logger = LogManager.getLogger();

    private final OrderServiceImpl orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting this session user to work with
        User user = (User) req.getSession().getAttribute("user");
        //getting service to work with database
        OrderService orderService = new OrderServiceImpl();
        try {
            //adding current user's basket(List<Product>) to request attribute as "basket"
            req.setAttribute("basket", orderService.getBasketProducts(user));
            //forward to baskter page
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/basket.jsp");
            requestDispatcher.forward(req, resp);
        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        //getting this session user and service to work with database
        actionsMap.get(ActionType.actionType(req)).accept(req, resp);
        doGet(req, resp);
    }

    private final Map<ActionType, BiConsumer<HttpServletRequest, HttpServletResponse>> actionsMap = Map.of(
        ActionType.PLUS, plus(),
        ActionType.MINUS, minus(),
        ActionType.GETORDER, getOrder(),
        ActionType.PRODUCTFORDELETE, deleteProduct()
    );

    private enum ActionType {
        PLUS,
        MINUS,
        GETORDER,
        PRODUCTFORDELETE;

        private static ActionType actionType(HttpServletRequest req) {
            for (ActionType value : values()) {
                final var parameter = req.getParameter(value.name().toLowerCase());
                if (parameter != null) {
                    return value;
                }
            }
            throw new NoSuchElementException();
        }
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> deleteProduct() {
        return (req, resp) -> {
            final var user = (User) req.getSession().getAttribute("user");
            String[] productsID = req.getParameterValues("productForDelete");
            for (String productID : productsID) {
                logger.info("User=" + user.getNickname() + " delete product from his basket");
                orderService.deleteProductFromBasket(user, Integer.parseInt(productID.trim()));
            }
        };
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> getOrder() {
        return (req, resp) -> {
            try {
                final var user = (User) req.getSession().getAttribute("user");
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
        return (req, resp) -> {
            orderService.updateBasket(true, (User) req.getSession().getAttribute("user"), Integer.parseInt(req.getParameter("plus")));
        };
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> minus() {
        return (req, resp) -> {
            orderService.updateBasket(false, (User) req.getSession().getAttribute("user"), Integer.parseInt(req.getParameter("minus")));
        };
    }
}

