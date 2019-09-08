package app.controller.servlets.storeservlets.admin;

import app.controller.servlets.AbstractServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class ListAdminServlet extends AbstractServlet {

    private enum ActionType {
        ADD_NEW_PRODUCT_NAME("name"),
        DELETE_PRODUCT("productForDelete");

        private String value;

        ActionType(String actionType) {
            value = actionType;
        }

        private static ActionType actionType(HttpServletRequest req) {
            for (ActionType value : values()) {
                if (req.getParameter(value.value)!=null && !req.getParameter(value.value).isEmpty()) {
                    return value;
                }
            }
            req.setAttribute("nullData", "nullData");
            return null;
        }
    }

    private final Map<ActionType, BiConsumer<HttpServletRequest, HttpServletResponse>> actionsMap = Map.of(
            ActionType.ADD_NEW_PRODUCT_NAME, add(),
            ActionType.DELETE_PRODUCT, delete()
    );


    private BiConsumer<HttpServletRequest, HttpServletResponse> add() {
        return (req, resp) -> {
            if(req.getParameter("price").isEmpty()){
                req.setAttribute("nullData", "");
            }else {
                var name = req.getParameter(ActionType.ADD_NEW_PRODUCT_NAME.value);
                //getting bundle with the exchange rates to convert dollars or euros to rubles
                var bundle = (ResourceBundle) req.getSession().getAttribute("bundle");
                int price = (int) (Integer.parseInt(req.getParameter("price")) / (Double) bundle.getObject("exchange.rates"));
                //add product to database
                productService.addNewProduct(name, price);
            }
        };
    }

    private BiConsumer<HttpServletRequest, HttpServletResponse> delete() {
        return (req, resp) -> {
            var products = req.getParameterValues(ActionType.DELETE_PRODUCT.value);
            Arrays.stream(products).forEach(p -> productService.deleteProduct(Integer.parseInt(p.trim())));
        };
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
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
        var act = ActionType.actionType(req);
        if (act != null) {
            var map = actionsMap.get(act);
            map.accept(req, resp);
        }
        doGet(req, resp);
    }
}
