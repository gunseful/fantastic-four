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

        User user = (User)req.getSession().getAttribute("user");


        Model model = Model.getInstance();
        if(req.getAttribute("exit")==null){
            req.setAttribute("orders", model.getOrders(user));
            System.out.println("Orders:");
            System.out.println(model.getOrders(user));
        }
        try {
            System.out.println("basket second try v1.0");
            if(!user.isAdministrator()){
                System.out.println("basket second try v2.0");

                req.setAttribute("loggin", user.getNickname());
                System.out.println("set attribute loggin");

                Model.getInstance().addToBasket(user, "");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/orders.jsp");
                requestDispatcher.forward(req, resp);

            }else {
                System.out.println("second try");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/orders.jsp");
                requestDispatcher.forward(req, resp);
            }

        }catch (Exception e){
            e.printStackTrace();

            }
        }





    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try{
//            if(req.getParameter("exit")!=null) {
//                System.out.println("exit");
//                req.setAttribute("exit", "exit");
//                doGet(req, resp);
//            }
//        }catch (Exception e){
//            System.out.println("not exit");
//        }
//
//        try{
//            if(req.getParameter("Orders")!=null) {
//                System.out.println("            GO TO ORDERS");
//                req.setAttribute("Orders", "Orders");
//                doGet(req, resp);
//            }
//        }catch (Exception e){
//            System.out.println("not goToBasket");
//        }
//
//        try{
//            if(req.getParameter("goToBasket")!=null) {
//                req.setAttribute("goToBasket", "goToBasket");
//                doGet(req, resp);
//            }
//        }catch (Exception e){
//            System.out.println("not goToBasket");
//        }

        User user = (User)req.getSession().getAttribute("user");

        try{
            if(req.getParameter("getOrder")!=null) {
                Model.getInstance().makeOrder(user);
                System.out.println("get Order");
                doGet(req, resp);
            }
        }catch (Exception e){
        }

          try {
            if (!req.getParameter("Pay").equals(null) && !req.getParameterValues("orderForDelete").equals(null)) {
                String[] ordersID = req.getParameterValues("orderForDelete");
                String basket = "";
                for (String orderID : ordersID) {
                    System.out.println(req.getParameter("Pay"));
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
                try{if(!req.getParameter("block").equals(null)){
                    Model.getInstance().addUserToBlackList(Model.getInstance().getUser(Integer.parseInt(req.getParameter("block"))));
//                    System.out.println(req.getParameter("block"));
//                    req.setAttribute("userPage", req.getParameter("block"));

                }
                }catch (Exception exept){
                    System.out.println("ну точно не дошли");
                    req.setAttribute("nullData", "");
                    doGet(req, resp);
                }

            }


        }

        doGet(req, resp);

    }
}
