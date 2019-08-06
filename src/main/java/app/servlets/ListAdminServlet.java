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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ListAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            if(req.getAttribute("exit")!=null) {
                Model.getInstance().setCurrentUser(null);
            }
        }catch (Exception e){

        }


        Model model = Model.getInstance();
        req.setAttribute("products", model.getList());
        try {
            if(model.getCurrentUser().isAdministrator()){
            req.setAttribute("loggin", model.getCurrentUser().getNickname());
            if (model.getCurrentUser().isAdministrator()) {
                req.setAttribute("admin", "");
            }

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/listAdmin.jsp");
            requestDispatcher.forward(req, resp);}else{resp.sendRedirect("/listClient");}

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
        System.out.println("list admin servlet");

        try{
            if(req.getParameter("exit")!=null) {
                req.setAttribute("exit", "exit");
                doGet(req, resp);
            }
        }catch (Exception e){
            System.out.println("not exit");
        }


        List<String> list = new ArrayList<>();
        try {
            if (!req.getParameterValues("productForDelete").equals(null)) {
                String[] productsID = req.getParameterValues("productForDelete");
                Model model = Model.getInstance();

                int i = 0;
                for (String productID : productsID) {
                    model.delete(productID.split(" ")[0]);
                    list.add((productID.split(" ")[1]));
                }
            }
        }catch (NullPointerException e){

            try{

            String name = req.getParameter("name");
            int price = Integer.parseInt(req.getParameter("price"));
            Product product = new Product(name, price);
            Model model = Model.getInstance();
            model.add(product);

            req.setAttribute("name", name);
            doGet(req, resp);

            }catch (Exception f){
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }

        }

        doGet(req, resp);

    }
}
