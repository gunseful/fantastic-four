package app.servlets;

import app.entities.Product;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Orders servlet");

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
            req.setAttribute("orders", model.getOrders());
            System.out.println(model.getCurrentUser().getBasket().getList());
        }
        try {
            System.out.println("basket second try v1.0");
            if(!model.getCurrentUser().isAdministrator()){
                System.out.println("basket second try v2.0");

                req.setAttribute("loggin", model.getCurrentUser().getNickname());
                System.out.println("set attribute loggin");

                Model.getInstance().addToBasket(Model.getInstance().getCurrentUser(), "");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/orders.jsp");
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

//        try{if(!req.getParameter("Pay").equals(null)){
//            System.out.println("                    pay");
//
//        }else{
//            System.out.println("                hehe pay");
//        }
//
//        }catch (Exception e){
//            System.out.println("              no pay");
//        }
//        System.out.println(req.getParameter("Pay"));

        try {
            if (!req.getParameter("Pay").equals(null) && !req.getParameterValues("orderForDelete").equals(null)) {
                String[] ordersID = req.getParameterValues("orderForDelete");
                String basket = "";
                for (String orderID : ordersID) {
                    System.out.println(orderID);
                    Model.getInstance().payOrder(Integer.parseInt(orderID));

                }
            }
        }catch (NullPointerException exeption){
            try {
                System.out.println("         Deleting order...");
                String[] ordersID = req.getParameterValues("orderForDelete");
                String basket = "";
                for (String orderID : ordersID) {
                    System.out.println(orderID);
                    Model.getInstance().deleteOrder(Integer.parseInt(orderID.trim()));

                }
            }catch (Exception e){
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }


        }

        doGet(req, resp);

    }
}
