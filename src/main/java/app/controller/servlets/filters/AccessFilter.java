package app.controller.servlets.filters;

import app.controller.servlets.AbstractServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter("/*")
public class AccessFilter extends AbstractServlet implements Filter{

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        var uri = req.getRequestURI();
        try {
            if(req.getSession().getAttribute("user")!=null) {
                var user = user(req);
                if (!availablePagesByRoles.get(user.getRole()).contains(uri)) {
                    res.sendError(403);
                }
            }
        filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException | ServletException e) {
            logger.error(e);
        }
    }
}
