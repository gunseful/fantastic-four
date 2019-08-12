
package app.servlets.prestoreservlets;

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LogoutServlet doPost");
            HttpSession session = request.getSession();
            if(request.getParameter("lang")!=null){
                session.setAttribute("ses", "session");
                ResourceBundle bundle = null;
                if(request.getParameter("lang").equals("Russian")){
                    Locale ru_loc = new Locale("ru", "RU");
                    bundle =
                            ResourceBundle.getBundle("app.locale.Language", ru_loc);

                }if(request.getParameter("lang").equals("English")){
                    Locale en_loc = new Locale("en", "EN");
                    bundle =
                            ResourceBundle.getBundle("app.locale.Language", en_loc);
                }

                request.setAttribute("bundle",bundle);
                request.getSession().putValue("bundle", bundle);
            }
            if (request.getParameter("Choose Language") != null) {
                request.getSession().setAttribute("ses", null);
            }else {
            }


            response.sendRedirect("/");


        }
    }
