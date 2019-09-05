package app.controller.servlets.prestoreservlets;

import app.controller.service.UserServiceImpl;
import app.controller.servlets.AbstractServlet;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationServlet extends AbstractServlet {
    public static Logger logger = LogManager.getLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/initialization/registration.jsp");
        requestDispatcher.forward(req, resp);
        }catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            //getting parameters "name" "nickname" and "password", if they are correct - trye to add new user, else - set attribute fail
            if (!req.getParameter("name").equals("") && req.getParameter("nickname").length()>=3 && req.getParameter("nickname").length()<=15 && req.getParameter("password").length()>=6 && req.getParameter("password").length()<=15) {
                User user = new User(req.getParameter("name"), req.getParameter("nickname").toUpperCase(), req.getParameter("password"));
                //if db has got user with this nickname - fail
                if(!userService.addNewUser(user)) {
                    req.setAttribute("fail", "");
                    logger.info("user"+user.getNickname()+" is already exist");
                }
                //if everything is fine redirect to loggin page
                logger.info("registration new user="+user.getNickname());
                resp.sendRedirect("/loggin");
                return;
            }else{
                logger.error("failed registration");
                req.setAttribute("fail", "");
            }
        }catch (Exception e){
            logger.error(e);
        }
        doGet(req, resp);
    }
}
