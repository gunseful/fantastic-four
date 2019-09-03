package app.controller.servlets.filters;

import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter("/*")
public class AccessFilter implements Filter {
    public static Logger logger = LogManager.getLogger(AccessFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("user");
        try {

        if (req.getSession().getAttribute("user") != null) {
            if (uri.equals("/loggin") || uri.equals("/registration")) {
                if(!user.isAdministrator()) {
                    res.sendRedirect("/listClient");
                }else{
                    res.sendRedirect("/listAdmin");
                }

            }
            if (uri.equals("/listAdmin") && !user.isAdministrator()) {
                    res.sendError(403);
            }
            if (uri.equals("/listClient") && user.isAdministrator()) {
                res.sendError(403);
            }
            if (uri.equals("/blacklist") && !user.isAdministrator()) {
                res.sendError(403);
            }
            if (uri.equals("/basket") && user.isAdministrator()) {
                res.sendError(403);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException | ServletException e) {
            logger.error(e);
        }
    }
}
