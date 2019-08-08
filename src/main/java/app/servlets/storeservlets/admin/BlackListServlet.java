package app.servlets.storeservlets.admin;

import app.entities.user.User;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BlackListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Black List Servlet do get");
        //получаем текущего юзера
        User user = (User)req.getSession().getAttribute("user");
        //получаем черный список спец методом класса Модель и передаем его в параметре блеклист
        req.setAttribute("blacklist", Model.getInstance().getBlackList());

        //на случай если сюда обычный клиент залезет (по идее никак не может, только если вручную впишет в браузере
        //не уверен юзаются ли в таком случае фильтры, если надо - скажи я постараюсь впихнуть
        //прост мне кажется такое решение довольно простым и очевидным
        try {
            if(!user.isAdministrator()){
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/listClient.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/blacklist.jsp");
                requestDispatcher.forward(req, resp);}
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Black List Servlet do post");
        //прилетает в виде параметров массив с айди юзеров которых надо удалить и вызываем спец метод из модели и удаляем из базы данных
        try {
            if (!req.getParameterValues("userForDelete").equals(null)) {
                String[] usersID = req.getParameterValues("userForDelete");
                Model model = Model.getInstance();
                for (String userID : usersID) {
                    model.deleteFromBlackList(Integer.parseInt(userID.trim()));
                }
            }
        }catch (NullPointerException ignored) {}
        doGet(req, resp);
    }
}
