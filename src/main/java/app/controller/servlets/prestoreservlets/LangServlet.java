
package app.controller.servlets.prestoreservlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/LangServlet")
public class LangServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //в зависимости от выбранного языка созадется бандл
        if (request.getParameter("lang") != null) {
            logger.info("selected " + request.getParameter("lang") + " language");
            Locale loc = new Locale(request.getParameter("lang"));
            ResourceBundle bundle =
                    ResourceBundle.getBundle("app.controller.locale.Language", loc);
            request.getSession().setAttribute("bundle", bundle);
        }
        response.sendRedirect(request.getParameter("jspname"));
    }
}
