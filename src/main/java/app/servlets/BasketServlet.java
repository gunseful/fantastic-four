package app.servlets;

import app.entities.Product;
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

        try{
            if(req.getAttribute("Orders")!=null) {
                resp.sendRedirect("/orders");
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
        if(req.getAttribute("exit")==null){
        req.setAttribute("basket", model.getCurrentUser().getBasket().getList());
            System.out.println(model.getCurrentUser().getBasket().getList());
        }
        try {
            System.out.println("basket second try v1.0");
            if(!model.getCurrentUser().isAdministrator()){
                System.out.println("basket second try v2.0");

                req.setAttribute("loggin", model.getCurrentUser().getNickname());
                System.out.println("set attribute loggin");

                Model.getInstance().addToBasket(Model.getInstance().getCurrentUser(), "");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/basket.jsp");
                requestDispatcher.forward(req, resp);

            }else{
                System.out.println("second try");
                resp.sendRedirect("/listBuyer");}

        }catch (Exception e){
            System.out.println("No current user");
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
                System.out.println("exit");
                req.setAttribute("exit", "exit");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not exit");
        }

        try{
            if(req.getParameter("Orders")!=null) {
                req.setAttribute("Orders", "Orders");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not goToBasket");
        }

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
                Model.getInstance().makeOrder();
                System.out.println("get Order");
                doGet(req, resp);
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
                            for (int i = 0; i < Model.getInstance().getCurrentUser().getBasket().getList().size(); i++) {
                                if (product.equals(Model.getInstance().getCurrentUser().getBasket().getList().get(i))) {
                                    Model.getInstance().getCurrentUser().getBasket().getList().remove(i);
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

