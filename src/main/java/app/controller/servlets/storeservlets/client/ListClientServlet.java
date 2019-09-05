package app.controller.servlets.storeservlets.client;

import app.controller.service.OrderServiceImpl;
import app.controller.service.ProductServiceImpl;
import app.controller.servlets.AbstractServlet;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListClientServlet extends AbstractServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting service to work with database
        ProductServiceImpl productService = new ProductServiceImpl();
        try {
            //getting product list with the productService and set request's attribute
            req.setAttribute("products", productService.getList());
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/listClient.jsp");
            requestDispatcher.forward(req, resp);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = user(req);
        OrderServiceImpl orderService = new OrderServiceImpl();
        //getting an array with products ID which client chose to buy (add to his basket)
        try {
            if (req.getParameterValues("productForBuy") != null) {
                for (String productId : req.getParameterValues("productForBuy")) {
                    //trying to update basket. If product with this id already exists in current user's basket - product just updating (count increment by 1)
                    if (!orderService.updateBasket(true, user, Integer.parseInt(productId.trim()))) {
                        //if can't update basket, just add new product to basket
                        orderService.addToBasket(user, Integer.parseInt(productId.trim()));
                    }
                }
            } else {
                logger.info("User=" + user.getNickname() + " chose nothing");
                req.setAttribute("nullData", "");
            }
        } catch (NullPointerException e) {
            logger.error(e);
        }
        doGet(req, resp);
    }
}
