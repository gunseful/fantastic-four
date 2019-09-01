package app.controller.servlets.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    public static Logger logger = LogManager.getLogger();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        //creating request and response
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        //getting session
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        //put into session default language bundle - en
        if (req.getSession().getAttribute("bundle") == null) {
            Locale loc = new Locale("en");
            ResourceBundle bundle =
                    ResourceBundle.getBundle("app.controller.locale.Language", loc);
            req.getSession().setAttribute("bundle", bundle);
        }
        try {

            //if there is no current user, which has logged in person who use webapp have access only to 3 pages - home, loggin and registration
        //else - all except as described above
        if (!isLoggedIn && !(uri.equals("/") || uri.endsWith("loggin") || uri.endsWith("registration") || uri.endsWith("LangServlet"))) {
            logger.error("Unauthorized access request");
//            this.context.log("Unauthorized access request");
                res.sendRedirect("/loggin");
            return;
        } else {
            if (isLoggedIn && (uri.equals("/"))) {
                res.sendRedirect("/loggin");
                return;
            }
        }

        chain.doFilter(request, response);

        } catch (IOException | ServletException e) {
            logger.error(e);
        }


    }

}
