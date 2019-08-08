package app.servlets.storeservlets.client;

import app.entities.products.Product;
import app.entities.user.User;
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
        //getting this session user to work with
        User user = (User)req.getSession().getAttribute("user");
        req.setAttribute("basket", user.getBasket().getList());
        //здесь проверяем является ли текущий юзер админом, если нет то обновляем корзину и переходим к вьюшке
        //не уверен надо ли юзать фильтры в таком случае...по идее админ никак сюда не попадает, только если в строке браузера впишет
        // / basket

        //обновляется корзина добавлением пустой строки в нее
        //вопрос - зачем. Может случится что удаляется товар какой-то с магаза, а в этом методе проверка есть ли она в базе данных
        //чтобы не висел в корзине левый товар и чтобы не писать отдельный метод, юзается такая хитрость
        try {
            if(!user.isAdministrator()){
                Model.getInstance().addToBasket(user, "");
                System.out.println("ПУСТАЯ КОРЗИНА - "+user.getBasket().getList().isEmpty());
//                if(user.getBasket().getList().isEmpty()){
//                    req.setAttribute("basket", null);
//                }
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/client/basket.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                resp.sendRedirect("/listAdmin");}
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //getting this session user to work with
        User user = (User)req.getSession().getAttribute("user");
        //when we click on bottom "Заказа" we give parameter getOrder and then making some order by special methid in Model.class and redirect to -> orders
        try{
            if(req.getParameter("getOrder")!=null) {
                Model.getInstance().makeOrder(user);
                resp.sendRedirect("/orders");
            }}catch (Exception ignored){}

        //we get some product for delete from the basket, taking products, checks is there in the db those product -> removing
        //if client choose nothing -> exception
        try {
            if (req.getParameterValues("productForDelete") != null) {
                String[] productsID = req.getParameterValues("productForDelete");
                for (String productID : productsID) {
                    System.out.println(productID);
                    for (Product product : Model.getInstance().getList()) {
                        if (String.valueOf(product.getId()).equals(productID.trim())) {
                            for (int i = 0; i < user.getBasket().getList().size(); i++) {
                                if (product.equals(user.getBasket().getList().get(i))) {
                                    user.getBasket().getList().remove(i);
                                }
                            }
                        }
                    }
                }
            }
        }catch (NullPointerException exception){
            System.out.println("нул дата");
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        doGet(req, resp);
    }
}

