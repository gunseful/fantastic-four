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

public class AddNewUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getAttribute("loggin") != null) {
                resp.sendRedirect("/loggin");
            } else {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/addNewUser.jsp");
                requestDispatcher.forward(req, resp);
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!req.getParameterValues("name").equals(null) && !req.getParameterValues("nickname").equals(null) && !req.getParameterValues("password").equals(null)) {
                User user = new User(req.getParameter("name"), req.getParameter("nickname").toUpperCase(), req.getParameter("password"));
                Model model = Model.getInstance();
                if(model.addNewUser(user)==false) {
                    req.setAttribute("nickIsBusy", "");
                    doGet(req, resp);
                }
                    req.setAttribute("loggin", user.getName());

                }else{
            }
        }catch (NullPointerException e){

        }
        doGet(req, resp);

    }
}
