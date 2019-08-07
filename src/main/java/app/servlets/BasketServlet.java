package app.servlets;

import app.entities.Product;
import app.entities.User;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasketServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("basket buyer servlet");

        Model model = Model.getInstance();
        User user = (User)req.getSession().getAttribute("user");
        if(req.getAttribute("exit")==null){
        req.setAttribute("basket", user.getBasket().getList());
        }
        try {
            if(!user.isAdministrator()){
                System.out.println("add to basket");
                Model.getInstance().addToBasket(user, "");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/basket.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                resp.sendRedirect("/listAdmin");}
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession().getAttribute("user");

                try{
            if(req.getParameter("goToBasket")!=null) {
                req.setAttribute("goToBasket", "goToBasket");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not goToBasket");
        }

        try{
            if(req.getParameter("getOrder")!=null) {
                System.out.println("hehe");
                Model.getInstance().makeOrder(user);
                resp.sendRedirect("/orders");
            }
        }catch (Exception e){
        }


        try {
            if (!req.getParameterValues("productForDelete").equals(null)) {
                String[] productsID = req.getParameterValues("productForDelete");

                String basket = "";
                for (String productID : productsID) {
                    System.out.println(productID);
                    for (Product product : Model.getInstance().getList()) {
                        if (String.valueOf(product.getId()).equals(productID.split(" ")[0])) {
                            for (int i = 0; i < user.getBasket().getList().size(); i++) {
                                if (product.equals(user.getBasket().getList().get(i))) {
                                    user.getBasket().getList().remove(i);
                                }
                            }
                        }
                    }
                }
            }
        }catch (NullPointerException exeption){
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        doGet(req, resp);
    }
}

