package app.servlets;

import app.entities.User;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Model model = Model.getInstance();
        List<User> pacani = model.getList();
        req.setAttribute("pacani", pacani);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/list.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> list = new ArrayList<>();
        try {
            if (!req.getParameterValues("friend").equals(null)) {
                String[] usersID = req.getParameterValues("friend");
                Model model = Model.getInstance();

                int i = 0;
                for (String userID : usersID) {
                    model.delete(userID.split(" ")[0]);
                    list.add((userID.split(" ")[1]));
                }
            }
        }catch (NullPointerException e){

        }

        String[] array = new String[list.size()];
        list.toArray(array);
        if(list.size()>0) {
            req.setAttribute("deleted", array);

        }else{
            String s = "Вы не выбрали никого";
            req.setAttribute("NullPoint", s);
        }
        doGet(req, resp);

    }
}
