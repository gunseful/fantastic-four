
package app.servlets.prestoreservlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //создает сессию
        HttpSession session = request.getSession();
        if (request.getParameter("lang") != null) {
            //если язык выбран - указывает аттрибут сессии
            session.setAttribute("ses", "session");
            ResourceBundle bundle;
            //в зависимости от выбранного языка созадется бандл
            logger.info("selected "+ request.getParameter("lang")+" language");
            Locale loc = new Locale(request.getParameter("lang"));
            bundle =
                    ResourceBundle.getBundle("app.locale.Language", loc);
            request.getSession().setAttribute("bundle", bundle);
        }
        //если нажимается кнопка выбора языка вьюшка меняется
        if (request.getParameter("Choose Language") != null) {
            request.getSession().setAttribute("ses", null);
        }
        response.sendRedirect("/");
    }
}
