package app.controller.servlets.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    public static Logger logger = LogManager.getLogger();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //сервлеты создаем
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        //мутим сессию
        HttpSession session = req.getSession(false);

        //если сессии нет либо нет юзера в сессии то фелс, а так тру
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        //если никто не залогинился, если страница не хоум и не логин или регистрация
        //то переводит на страницу логина
        if (!isLoggedIn && !(uri.equals("/") || uri.endsWith("loggin") || uri.endsWith("registration") || uri.endsWith("LangServlet"))) {
            logger.error("Unauthorized access request");
//            this.context.log("Unauthorized access request");
            res.sendRedirect("/loggin");
            return;
        }
        chain.doFilter(request, response);


    }

    public void destroy() {
    }

}
