package app.servlets;

import app.entities.Product;
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

public class LogginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            if (Model.getInstance().getCurrentUser().isAdministrator()) {
                resp.sendRedirect("/listAdmin");
            }else{
                resp.sendRedirect("/listBuyer");
            }
        } catch (Exception e) {

        if (req.getAttribute("loggin") != null) {
            resp.sendRedirect("/listAdmin");
        } else {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/loggin.jsp");
            requestDispatcher.forward(req, resp);
        }
    }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");

            User user = new User(nickname, password);
            Model model = Model.getInstance();

            if(model.checkLogginAndPassword(user)){
                req.setAttribute("loggin", Model.getInstance().getCurrentUser().getName());
                doGet(req, resp);
            }else{
                req.setAttribute("NoData","NoData");
                System.out.println("not found user with this nickname");
                doGet(req, resp);
            }

        }catch (Exception e){
            req.setAttribute("NoData","NoData");
            doGet(req, resp);
        }


    }
}
