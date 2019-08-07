package app.servlets;

import app.entities.Product;
import app.entities.User;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LogginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/loggin.jsp");
            requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("loggin servlet");
        try {
            String nickname = req.getParameter("nickname");
            String password = req.getParameter("password");

            User user = new User(nickname, password);
            Model model = Model.getInstance();

            if(model.checkLogginAndPassword(user)){
                User currentUser = Model.getInstance().getUserByNickName(nickname);
                if(Model.getInstance().checkBlackList(currentUser)){
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/views/loggin.jsp");
                    resp.setContentType("text/html;charset=UTF-8");
                    req.setAttribute("inBlackList", currentUser.getNickname());
                    rd.include(req , resp);
                }
                System.out.println("adding session");
                HttpSession session = req.getSession();
                session.setAttribute("user", currentUser);
                session.setMaxInactiveInterval(30*60);
                Cookie userName = new Cookie("user", nickname);
                userName.setMaxAge(30*60);
                resp.addCookie(userName);
                System.out.println(currentUser);
                if(currentUser.isAdministrator()) {
                    System.out.println("redirect to listAdmin");
                    resp.sendRedirect("/listAdmin");
                }else{
                    System.out.println("redirect to listClient");
                    resp.sendRedirect("/listClient");
                }

            }else{
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/views/loggin.jsp");
                resp.setContentType("text/html;charset=UTF-8");
                req.setAttribute("NoData", "NoData");
                rd.include(req , resp);
            }

        }catch (Exception e){
                e.printStackTrace();
                req.setAttribute("NoData", "NoData");
                doGet(req, resp);
        }


    }
}
