package app.controller.servlets.prestoreservlets;

import app.controller.service.UserServiceImpl;
import app.controller.servlets.AbstractServlet;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
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
        var nick = req.getParameter("name");
        var pass = req.getParameter("password");
        try {
            //getting parameters "name" "nickname" and "password", if they are correct - true to add new user, else - set attribute fail
            if (!nick.isEmpty() && nick.length()>=3 && nick.length()<=15 &&
                    pass.length()>=6 && pass.length()<=15) {
                var user = new User(req.getParameter("name"), nick.toUpperCase(), pass);
                if(userService.addNewUser(user)) {
                    logger.info("registration new user = {}", user.getNickname());
                    resp.sendRedirect("/loggin");
                    return;
                }
                //if db has got user with this nickname - fail
                req.setAttribute("alreadyExist", user.getNickname());
                logger.info("user = {} is already exist", user.getNickname());
            }else{
                logger.error("failed registration");
                req.setAttribute("fail", "fail");
            }
        }catch (Exception e){
            logger.error(e);
        }
        doGet(req, resp);
    }
}
