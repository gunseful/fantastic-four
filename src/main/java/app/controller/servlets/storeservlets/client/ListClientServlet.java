package app.controller.servlets.storeservlets.client;

import app.controller.servlets.AbstractServlet;
import app.model.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class ListClientServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting service to work with database
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
        //getting an array with products ID which client chose to buy (add to his basket)
        try {
            var products = req.getParameterValues("productForBuy");
            if (products != null) {
                //trying to update basket. If product with this id already exists in current user's basket - product just updating (count increment by 1)
                //if can't update basket, just add new product to basket
                Arrays.stream(products)
                        .filter(p -> !orderService.increaseCount(user, Integer.parseInt(p.trim())))
                        .forEach(p -> orderService.addToBasket(user, Integer.parseInt(p.trim())));
            }else{
            logger.info("User= {} chose nothing", user.getNickname());
            req.setAttribute("nullData", user.getNickname());}
        } catch (NullPointerException e) {logger.error(e);}
        doGet(req, resp);
    }
}
