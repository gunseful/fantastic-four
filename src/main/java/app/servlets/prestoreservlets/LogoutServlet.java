
package app.servlets.prestoreservlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LogoutServlet doPost");
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        //ну этот метод вызываем только чтобы обнулить куки и сессию
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("JSESSIONID")){
                    System.out.println("JSESSIONID="+cookie.getValue());
                    break;
                }
            }
        }
        //ну вот обнуляем кароче и редиректимкся на хоум пейдж
        HttpSession session = request.getSession(false);
        System.out.println("User="+session.getAttribute("user"));
        session.invalidate();
        response.sendRedirect("/");
    }
}
