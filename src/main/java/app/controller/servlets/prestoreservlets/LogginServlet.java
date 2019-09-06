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

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/initialization/loggin.jsp");
            requestDispatcher.forward(req, resp);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");
        userService.authorize(nickname, password).ifPresentOrElse(user -> {
            proceedUser(req, resp, nickname, user);
        }, () -> {
            logger.error("User= {} is not found in database", nickname);
            req.setAttribute("NoData", "NoData");
        });

        doGet(req, resp);
    }

    private void proceedUser(HttpServletRequest req, HttpServletResponse resp, String nickname, User user) {
        HttpSession session = req.getSession();
        if (userService.checkBlackList(user)) {
            logger.error("User={} is in black list", nickname);
            req.setAttribute("inBlackList", nickname);
        } else {
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);
            Cookie userName = new Cookie("user", nickname);
            userName.setMaxAge(30 * 60);
            resp.addCookie(userName);
            logger.info("User=" + nickname + " has been log in");
            try {
                if (user.isAdministrator()) {
                    resp.sendRedirect("/listAdmin");
                } else {
                    resp.sendRedirect("/listClient");
                }
            } catch (Exception e) {
                logger.error("failed log in", e);
            }
        }
    }
}
