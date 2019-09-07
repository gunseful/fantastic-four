package app.controller.servlets;

import app.controller.service.OrderServiceImpl;
import app.controller.service.ProductServiceImpl;
import app.controller.service.UserServiceImpl;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public abstract class AbstractServlet extends HttpServlet {

    private static final String USER = "user";
    public static Logger logger = LogManager.getLogger();

    protected final OrderServiceImpl orderService = new OrderServiceImpl();
    protected final UserServiceImpl userService = new UserServiceImpl();
    protected final ProductServiceImpl productService = new ProductServiceImpl();

    protected static final Map<String, String> homePagesByRoles = Map.of(
            "ADMIN", "/listAdmin",
            "USER", "/listClient");

    protected static final Set<String> availablePagesPagesBeforeLogin = Set.of("/loggin", "/registration", "/LangServlet", "/");

    protected static final Map<String, Set<String>> map = Map.of(
            "ADMIN", Set.of("/listAdmin", "/blacklist", "/orders", "/LangServlet", "/LogoutServlet", "/loggin"),
            "USER", Set.of("/listClient", "/basket", "/orders", "/LangServlet", "/LogoutServlet", "/loggin"));

    protected User user(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(USER);
    }
}
