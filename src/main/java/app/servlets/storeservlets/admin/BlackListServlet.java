package app.servlets.storeservlets.admin;

import app.entities.user.User;
import app.model.controller.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlackListServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //получаем текущего юзера
        User user = (User)req.getSession().getAttribute("user");
        Repository controller = (Repository) req.getSession().getAttribute("controller");
        //получаем черный список спец методом класса Модель и передаем его в параметре блеклист
        req.setAttribute("blacklist", controller.getBlackList());

        try {
            if(!user.isAdministrator()){
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/listClient.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/admin/blacklist.jsp");
                requestDispatcher.forward(req, resp);}
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Repository controller = (Repository)req.getSession().getAttribute("controller");
        //прилетает в виде параметров массив с айди юзеров которых надо удалить и вызываем спец метод из модели и удаляем из базы данных
        try {
            if (req.getParameterValues("userForDelete") != null) {
                String[] usersID = req.getParameterValues("userForDelete");
                for (String userID : usersID) {
                    logger.info("Administrator is trying to delete user from blacklist");
                    controller.deleteFromBlackList(Integer.parseInt(userID.trim()));
                }
            }
        }catch (NullPointerException ignored) {}
        doGet(req, resp);
    }
}
