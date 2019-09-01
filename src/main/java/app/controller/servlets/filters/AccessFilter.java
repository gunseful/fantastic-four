package app.controller.servlets.filters;

import app.model.user.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter("/*")
public class AccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("user");

        if (req.getSession().getAttribute("user") != null) {
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
    }
}
