package app.controller.servlets.storeservlets.admin;

import app.controller.service.ProductServiceImpl;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

public class ListAdminServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting service to work with database
        ProductServiceImpl productService = new ProductServiceImpl();
        try {
            //getting product list with the productService and set request's attribute
            req.setAttribute("products", productService.getList());
            //forward to listAdmin page
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/admin/listAdmin.jsp");
            requestDispatcher.forward(req, resp);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        ProductServiceImpl productService = new ProductServiceImpl();
        try {
            //getting an array with products ID which admin chose to delete
            if (req.getParameterValues("productForDelete") != null) {
                String[] productsID = req.getParameterValues("productForDelete");
                for (String productID : productsID) {
                    productService.deleteProduct(Integer.parseInt(productID.trim()));
                    logger.info("User=" + user.getNickname() + " has been deleted " + productID);
                }
            } else if (req.getParameter("name") != null && req.getParameter("price") != null) {

                //if admin trying to add new product - getting name and price
                    String name = req.getParameter("name");
                    //getting bundle with the exchange rates to convert dollars or euros to rubles
                    ResourceBundle bundle = (ResourceBundle) req.getSession().getAttribute("bundle");
                    Double d = (Double) bundle.getObject("exchange.rates");
                    int price = (int) (Integer.parseInt(req.getParameter("price")) / d);
                    //add product to database
                    productService.addNewProduct(name, price);
                    logger.info("User=" + user.getNickname() + " has been added new product - " + name);
                } else {
                    req.setAttribute("nullData", "");
                }
            } catch(NullPointerException e){
                logger.error(e);
            }
            doGet(req, resp);
        }
    }
