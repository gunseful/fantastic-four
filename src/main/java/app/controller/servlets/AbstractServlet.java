package app.controller.servlets;

import app.model.user.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractServlet extends HttpServlet {

    protected static final String USER = "user";

    protected User user(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(USER);
    }
}
