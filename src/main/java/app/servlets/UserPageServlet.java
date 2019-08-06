package app.servlets;

import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("UserPage servlet");
        System.out.println(req.getParameter("userPage"));



        try{
            if(req.getAttribute("exit")!=null) {
                Model.getInstance().setCurrentUser(null);

            }
        }catch (Exception e){

        }


                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/userPage.jsp");
                requestDispatcher.forward(req, resp);

    }




    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if(req.getParameter("exit")!=null) {
                System.out.println("exit");
                req.setAttribute("exit", "exit");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not exit");
        }


        try{
            if(req.getParameter("goToBasket")!=null) {
                req.setAttribute("goToBasket", "goToBasket");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not goToBasket");
        }

        doGet(req, resp);

    }
}
