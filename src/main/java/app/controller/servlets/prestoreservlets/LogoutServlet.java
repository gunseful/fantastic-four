
package app.controller.servlets.prestoreservlets;

import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("JSESSIONID")){
                    logger.info("JSESSIONID= "+cookie.getValue());
                    break;
                }
            }
        }
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        logger.info("User="+user.getNickname()+" has out");
        session.invalidate();
        response.sendRedirect("/");
    }
}
