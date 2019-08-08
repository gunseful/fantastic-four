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
        System.out.println("ListClientServlet doGet");
        //получаем юзера
        User user = (User)req.getSession().getAttribute("user");

        //получаем лист товаров
        req.setAttribute("products", Model.getInstance().getList());
        System.out.println(Model.getInstance().getList());
        //проверяем, если админ сюда зайдет его кинет на страницу админа, если обычный клиент, открывает вьюшку
        try {
            if(!user.isAdministrator()) {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/listClient.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                resp.sendRedirect("/listAdmin");}
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ListClientServlet doPost");

        //выбираем в списке чо хотим купить, то есть добавить в корзину и кликаем - > Летит в корзину
        try {
            if (req.getParameterValues("productForBuy") != null) {
                String[] productsID = req.getParameterValues("productForBuy");
                //опять юзера из сессии достаем
                User user = (User)req.getSession().getAttribute("user");
                for (String productID : productsID) {
                    System.out.println(productID.trim());
                    for(Product product : Model.getInstance().getList()){
                        //проверяем есть ли ваще продукт в базе данных
                        if(String.valueOf(product.getId()).equals(productID.trim())){
                            user.getBasket().getList().add(product);
                            //если есть добавляет в базу данных
                            Model.getInstance().addToBasket(user, product.getId()+" ");
                            }
                    }
                }
            }else{
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }
        }catch (NullPointerException e){
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        //ну прост обновляем вьюшку
        doGet(req, resp);
    }
}
