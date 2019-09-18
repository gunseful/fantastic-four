
package app.controller.servlets.prestoreservlets;

import app.controller.servlets.AbstractServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/LangServlet")
public class LangServlet extends AbstractServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //depending on language add into session a language
        if (request.getParameter("lang") != null) {
            logger.info("selected {} language", request.getParameter("lang"));
            Locale loc = new Locale(request.getParameter("lang"));
            ResourceBundle bundle =
                    ResourceBundle.getBundle("app.controller.locale.Language", loc);
            request.getSession().setAttribute("bundle", bundle);
        }
        response.sendRedirect(request.getParameter("jspname"));
    }
}
