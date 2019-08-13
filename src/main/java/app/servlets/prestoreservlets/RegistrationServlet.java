package app.servlets.prestoreservlets;

import app.entities.user.User;
import app.model.controller.AbstractController;
import app.model.controller.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //проверяем есть ли текущий пользователь, если есть то пропускаем регистрацию и переходим на страницу логина
        //так как мы уже залогинены нас бросит на страницу либо админа либо покупателя
        User user = (User)req.getSession().getAttribute("user");
        try {
            if (user != null) {
                logger.info(user.getNickname()+" was redirected to the store");
                resp.sendRedirect("/loggin");
            } else {
                //если текущего пользователя нет, проверим передан ли атрибут "зарегестрирован", если да - то прыгаем на страницу логина
                //если нет то снова на страницу регистрации
                if(req.getAttribute("registered") == null) {
                    logger.info("registration is succesful");
                    RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/initialization/registration.jsp");
                    requestDispatcher.forward(req, resp);
                }else{
                    resp.sendRedirect("/loggin");
                }
            }
        }catch (Exception ignored) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AbstractController controller = new UserController();
        try {
            //Проверяем пришедшие параметры Имени, Ника и Пароль на правильность (или вообще наличие).
            if (!req.getParameter("name").equals("") && req.getParameter("nickname").length()>=3 && req.getParameter("nickname").length()<=15 && req.getParameter("password").length()>=6 && req.getParameter("password").length()<=15) {
                // Они оказались верны. Создаем нового юзера
                User user = new User(req.getParameter("name"), req.getParameter("nickname").toUpperCase(), req.getParameter("password"));
                //Проверяем есть ли юзер с таким именем в базе, если есть - ошибка. Если нет - Прыгаем на страницу Логина
                if(!controller.addNewUser(user)) {
                    logger.info("user"+user.getNickname()+"is already exist");
                    req.setAttribute("fail", "");
                    doGet(req, resp);
                }
                logger.info("registration new user="+user.getNickname());
                req.setAttribute("registered", user.getName());
                doGet(req, resp);
            }else{
                //если пришедшие в параметрах данные не верны выдаем ошибку
                req.setAttribute("fail", "");
                doGet(req, resp);
            }
        }catch (NullPointerException ignored){
        }
        controller=null;
        doGet(req, resp);
    }
}
