package app.servlets.prestoreservlets;

import app.entities.user.User;
import app.model.controller.AbstractController;
import app.model.controller.UserController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LogginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LogginServlet doGet");
        User user = (User)req.getSession().getAttribute("user");
        //если чел уже залогинился, его бросает сразу на страницу магазина
        if(user!=null) {
            if (user.isAdministrator()) {
                    resp.sendRedirect("/listAdmin");
                } else {
                    resp.sendRedirect("/listClient");
            }
        }else{
            //прост бросает на вьюшку
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/initialization/loggin.jsp");
            requestDispatcher.forward(req, resp);}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LogginServlet doPost");

        //прилетает то что было в полях, логи и пароль, чтобы зайти, создается пользователь промежуточный
        try {
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");
            //вот здесь
            User user = new User(nickname, password);
            HttpSession session = req.getSession();
            session.setAttribute("controller", new UserController());
            AbstractController controller = (UserController)req.getSession().getAttribute("controller");
            //опять таки нужна нам наша модель, для работы с базой данной только ее и будет юзать
            //ну и проверяем логин и пароль, ничего не хэшируется, тупо строкой пароль идет - небезопасно офк, ну а как еще
            if(controller.checkLogginAndPassword(user)){
                User currentUser = controller.getUserByNickName(nickname);
                //проверяется, а не в черном ли списке чувак
                if(controller.checkBlackList(currentUser)){
                    //если да то бросает опять на логин с атрибутом inBlackList (вылезит уведомление)
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/views/initialization/loggin.jsp");
                    resp.setContentType("text/html;charset=UTF-8");
                    req.setAttribute("inBlackList", currentUser.getNickname());
                    rd.include(req , resp);
                    doGet(req,resp);
                }else {
                    //если все ок, и не в черном списке, то добавляем сессию, туда кидает юзера, устанавливаем интервал инактивности
                    //добавляем юзера в куки
                    session.setAttribute("user", currentUser);
                    session.setMaxInactiveInterval(30 * 60);
                    Cookie userName = new Cookie("user", nickname);
                    userName.setMaxAge(30 * 60);
                    resp.addCookie(userName);
                    //если юзер админ редиректим на лист админа
                    if (currentUser.isAdministrator()) {
                        resp.sendRedirect("/listAdmin");
                    } else {
                        //если обычный клиент, бросаем его на лист Клиента
                        resp.sendRedirect("/listClient");
                    }
                }
            }else{
                //если такого юзера в базе нет, кидает опять в логин с ошибкой ноудата
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/views/initialization/loggin.jsp");
                resp.setContentType("text/html;charset=UTF-8");
                req.setAttribute("NoData", "NoData");
                rd.include(req , resp);
            }

        }catch (Exception ignored){
            //если такого юзера в базе нет, кидает опять в логин с ошибкой ноудата
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/views/initialization/loggin.jsp");
            resp.setContentType("text/html;charset=UTF-8");
            req.setAttribute("NoData", "NoData");
            rd.include(req , resp);
        }


    }
}
