
package app.servlets.prestoreservlets;

import app.entities.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public static Logger logger = LogManager.getLogger();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        //ну этот метод вызываем только чтобы обнулить куки и сессию
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("JSESSIONID")){
                    logger.info("JSESSIONID= "+cookie.getValue());
                    break;
                }
            }
        }
        //ну вот обнуляем кароче и редиректимкся на хоум пейдж
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        logger.info("User="+user.getNickname()+" has out");
        session.invalidate();
        response.sendRedirect("/");
    }
}
