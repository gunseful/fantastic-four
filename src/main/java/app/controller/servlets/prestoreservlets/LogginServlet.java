package app.controller.servlets.prestoreservlets;

import app.controller.service.UserService;
import app.controller.service.UserServiceImpl;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;

public class LogginServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/initialization/loggin.jsp");
            requestDispatcher.forward(req, resp);
        }catch (Exception e){
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = new UserServiceImpl();

        try {
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");
            User user = new User(nickname, password);
            HttpSession session = req.getSession();
            if (userService.authorize(nickname, password)) {
                User currentUser = userService.getUserByNickname(nickname.toUpperCase());
                if (userService.checkBlackList(currentUser)) {
                    logger.error("User= " + currentUser.getNickname() + " is in black list");
                    req.setAttribute("inBlackList", currentUser.getNickname());
                } else {
                    session.setAttribute("user", currentUser);
                    session.setMaxInactiveInterval(30 * 60);
                    Cookie userName = new Cookie("user", nickname);
                    userName.setMaxAge(30 * 60);
                    resp.addCookie(userName);
                    logger.info("User=" + user.getNickname() + " has been log in");
                    if (currentUser.isAdministrator()) {
                        resp.sendRedirect("/listAdmin");
                        return;
                    } else {
                        resp.sendRedirect("/listClient");
                        return;
                    }
                }
            } else {
                logger.error("User= " + user.getNickname() + " is not found in database");
                req.setAttribute("NoData", "NoData");
            }
        } catch (Exception e) {
            logger.error("failed log in", e);
        }
        doGet(req, resp);
    }
}
