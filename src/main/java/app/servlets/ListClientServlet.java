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
        System.out.println("list buyer servlet");

        try{
            if(req.getAttribute("goToBasket")!=null) {
                resp.sendRedirect("/basket");
            }
        }catch (Exception e){

        }

        try{
            if(req.getAttribute("exit")!=null) {
                Model.getInstance().setCurrentUser(null);

            }
        }catch (Exception e){

        }




        Model model = Model.getInstance();
        req.setAttribute("products", model.getList());
        try {
            System.out.println("second try v1.0");
            if(!model.getCurrentUser().isAdministrator()){
                System.out.println("second try v2.0");

                req.setAttribute("loggin", model.getCurrentUser().getNickname());
                System.out.println("set attribute loggin");

                Model.getInstance().addToBasket(Model.getInstance().getCurrentUser(), "");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/listClient.jsp");
                requestDispatcher.forward(req, resp);

            }else{
                System.out.println("second try");
                resp.sendRedirect("/listAdmin");}

        }catch (Exception e){
            System.out.println("No current user");
            e.printStackTrace();
            try {
                resp.sendRedirect("/loggin");
            }catch (Exception x){

            }
        }





    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if(req.getParameter("exit")!=null) {
                req.setAttribute("exit", "exit");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not exit");
        }

        try{
            if(req.getParameter("goToBasket")!=null) {
                req.setAttribute("goToBasket", "goToBasket");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not goToBasket");
        }


        try {
            if (!req.getParameterValues("productForBuy").equals(null)) {
                String[] productsID = req.getParameterValues("productForBuy");

                String basket = "";
                for (String productID : productsID) {
                    System.out.println(productID);
                    for(Product product : Model.getInstance().getList()){
                        if(String.valueOf(product.getId()).equals(productID.split(" ")[0])){
                            Model.getInstance().getCurrentUser().getBasket().getList().add(product);
                            Model.getInstance().addToBasket(Model.getInstance().getCurrentUser(), product.getId()+" ");
                            System.out.println(Model.getInstance().getCurrentUser().getBasket().getList());

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
