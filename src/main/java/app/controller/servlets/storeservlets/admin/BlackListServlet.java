package app.controller.servlets.storeservlets.admin;

import app.controller.service.UserService;
import app.controller.service.UserServiceImpl;
import app.controller.servlets.AbstractServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class BlackListServlet extends AbstractServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //getting service to work with database
        UserService userService = new UserServiceImpl();
        try {
            //getting blacklist with the userService and set request's attribute
            req.setAttribute("blacklist", userService.getBlackList());
            //forward to blacklist page
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/admin/blacklist.jsp");
            requestDispatcher.forward(req, resp);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        //getting UserService to work with database
        UserService userService = new UserServiceImpl();
        try {
            //getting parameters from "usersForDelete" as an array
            var users = req.getParameterValues("userForDelete");
            if (users != null) {
                Arrays.stream(users)
                        .forEach(u ->  userService.removeFromBlackList(Integer.parseInt(u.trim())));
            }else{
                req.setAttribute("nullData", "");
            }
        } catch (NullPointerException e) {
            logger.error(e);
        }
        doGet(req, resp);
    }
}
