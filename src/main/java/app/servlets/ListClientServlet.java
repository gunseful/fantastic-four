package app.servlets;

import app.entities.Basket;
import app.entities.Product;
import app.entities.User;
import app.model.Model;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListClientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("List Client Servlet");


        try{
            if(req.getAttribute("goToBasket")!=null) {
                resp.sendRedirect("/basket");
            }
        }catch (Exception e){

        }

        req.setAttribute("products", Model.getInstance().getList());
        User user = (User)req.getSession().getAttribute("user");
        System.out.println(user);
        try {
            if(!user.isAdministrator()) {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/listClient.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                resp.sendRedirect("/listAdmin");}

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed...");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!req.getParameterValues("productForBuy").equals(null)) {
                String[] productsID = req.getParameterValues("productForBuy");
                User user = (User)req.getSession().getAttribute("user");
                String basket = "";
                for (String productID : productsID) {
                    System.out.println(productID);
                    for(Product product : Model.getInstance().getList()){
                        if(String.valueOf(product.getId()).equals(productID.split(" ")[0])){
                            user.getBasket().getList().add(product);
                            Model.getInstance().addToBasket(user, product.getId()+" ");
                            }
                    }
                }
            }
        }catch (NullPointerException e){
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        doGet(req, resp);
    }
}
