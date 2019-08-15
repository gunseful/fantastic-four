
package app.servlets.prestoreservlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet("/LangServlet")
public class LangServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //создает сессию
        HttpSession session = request.getSession();
        if (request.getParameter("lang") != null) {
            //если язык выбран - указывает аттрибут сессии
            session.setAttribute("ses", "session");
            ResourceBundle bundle = null;
            //в зависимости от выбранного языка созадется бандл
            if (request.getParameter("lang").equals("Russian")) {
                logger.info("selected russian language");
                Locale ru_loc = new Locale("ru", "RU");
                bundle =
                        ResourceBundle.getBundle("app.locale.Language", ru_loc);
            }
            if (request.getParameter("lang").equals("English")) {
                logger.info("selected english language");

                Locale en_loc = new Locale("en", "EN");
                bundle =
                        ResourceBundle.getBundle("app.locale.Language", en_loc);
            }
            if (request.getParameter("lang").equals("French")) {
                logger.info("selected english language");

                Locale fr_loc = new Locale("fr", "FR");
                bundle =
                        ResourceBundle.getBundle("app.locale.Language", fr_loc);
            }
            //бандл передается аттрибутом в сессию
            request.getSession().putValue("bundle", bundle);
        }
        //если нажимается кнопка выбора языка вьюшка меняется
        if (request.getParameter("Choose Language") != null) {
            request.getSession().setAttribute("ses", null);
        } else {
        }


        response.sendRedirect("/");


    }
}
