package app.controller.servlets.filters;

import app.controller.servlets.AbstractServlet;
import app.model.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter("/*")
public class AccessFilter extends AbstractServlet implements Filter{
    public static Logger logger = LogManager.getLogger(AccessFilter.class);



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        try {
            if(req.getSession().getAttribute("user")!=null) {
                User user = (User) req.getSession().getAttribute("user");
                if (!map.get(user.getRole()).contains(uri)) {
                    res.sendError(403);
                }
            }
        filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException | ServletException e) {
            logger.error(e);
        }
    }
}
