
package app.controller.servlets.prestoreservlets;

import app.controller.servlets.AbstractServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends AbstractServlet {
    public static Logger logger = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("JSESSIONID")){
                    logger.info("JSESSIONID={}",cookie.getValue());
                    break;
                }
            }
        }
        HttpSession session = request.getSession(false);
        var user = user(request);
        logger.info("User={} has out", user.getNickname());
        session.invalidate();
        response.sendRedirect("/");
    }
}
