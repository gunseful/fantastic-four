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

public class BlackListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Black List Servlet");
        try{
            if(req.getAttribute("Orders")!=null) {
                resp.sendRedirect("/orders");
            }
        }catch (Exception e){

        }




        User user = (User)req.getSession().getAttribute("user");
        if(req.getAttribute("exit")==null){
            req.setAttribute("blacklist", Model.getInstance().getBlackList());
        }
        try {
            if(!user.isAdministrator()){

                Model.getInstance().addToBasket(user, "");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/listAdmin.jsp");
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

        try{
            if(req.getParameter("Orders")!=null) {
                req.setAttribute("Orders", "Orders");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not goToBasket");
        }

        try{
            if(req.getParameter("goToBasket")!=null) {
                req.setAttribute("goToBasket", "goToBasket");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not goToBasket");
        }

        try {
            if (!req.getParameterValues("userForDelete").equals(null)) {
                String[] usersID = req.getParameterValues("userForDelete");
                Model model = Model.getInstance();
                for (String userID : usersID) {
                    model.deleteFromBlackList(Integer.parseInt(userID));
                }
            }
        }catch (NullPointerException e) {
        }

        doGet(req, resp);


    }
}
