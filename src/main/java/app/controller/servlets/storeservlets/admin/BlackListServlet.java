package app.controller.servlets.storeservlets.admin;

import app.model.user.User;
import app.controller.service.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class BlackListServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //получаем текущего юзера
        User user = (User) req.getSession().getAttribute("user");
        UserServiceImpl userService = new UserServiceImpl();
        //получаем черный список спец методом класса Модель и передаем его в параметре блеклист
        try {
            req.setAttribute("blacklist", userService.getBlackList());

            if (!user.isAdministrator()) {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/listClient.jsp");
                requestDispatcher.forward(req, resp);
            } else {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/admin/blacklist.jsp");
                requestDispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserServiceImpl userService = new UserServiceImpl();
        //прилетает в виде параметров массив с айди юзеров которых надо удалить и вызываем спец метод из модели и удаляем из базы данных
        try {
            if (req.getParameterValues("userForDelete") != null) {
                String[] usersID = req.getParameterValues("userForDelete");
                for (String userID : usersID) {
                    logger.info("Administrator is trying to delete user from blacklist");
                    userService.removeFromBlackList(Integer.parseInt(userID.trim()));
                }
            }
        } catch (NullPointerException | SQLException ignored) {
        }
        doGet(req, resp);
    }
}
