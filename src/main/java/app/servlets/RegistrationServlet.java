package app.servlets;

import app.entities.User;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //проверяем есть ли текущий пользователь, если есть то пропускаем регистрацию и переходим на страницу логина
        //так как мы уже залогинены нас бросит на страницу либо админа либо покупателя
        User user = (User)req.getSession().getAttribute("user");
        try {
            if (user != null) {
                resp.sendRedirect("/loggin");
            } else {
                //если текущего пользователя нет, проверим передан ли атрибут "зарегестрирован", если да - то прыгаем на страницу логина
                //если нет то снова на страницу регистрации
                if(req.getAttribute("registered") == null) {
                    RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/registration.jsp");
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
        try {
            //Проверяем пришедшие параметры Имени, Ника и Пароль на правильность (или вообще наличие).
            if (!req.getParameter("name").equals("") && req.getParameter("nickname").length()>3 && req.getParameter("nickname").length()<15 && req.getParameter("password").length()>6 && req.getParameter("password").length()<15) {
                // Они оказались верны. Создаем нового юзера
                User user = new User(req.getParameter("name"), req.getParameter("nickname").toUpperCase(), req.getParameter("password"));
                //Проверяем есть ли юзер с таким именем в базе, если есть - ошибка. Если нет - Прыгаем на страницу Логина
                if(!Model.getInstance().addNewUser(user)) {
                    req.setAttribute("fail", "");
                    doGet(req, resp);
                }
                req.setAttribute("registered", user.getName());
                doGet(req, resp);
            }else{
                //если пришедшие в параметрах данные не верны выдаем ошибку
                req.setAttribute("fail", "");
                doGet(req, resp);
            }
        }catch (NullPointerException ignored){
        }
        doGet(req, resp);
    }
}
