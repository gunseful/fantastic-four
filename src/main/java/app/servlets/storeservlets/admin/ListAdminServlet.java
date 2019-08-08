package app.servlets.storeservlets.admin;

import app.entities.products.Product;
import app.entities.user.User;
import app.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ListAdminServlet doGet");
        //как всегда берем юзера из сесси для работы с ним
        User user = (User)req.getSession().getAttribute("user");
        //кидаем все продукты из базы данных аттрибутом
        req.setAttribute("products", Model.getInstance().getList());
        //проверяем не залез ли шпион и переводим на страницу клиента если залез все таки
        try {
            if(user.isAdministrator()) {
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/listAdmin.jsp");
                requestDispatcher.forward(req, resp);
            }else{
                resp.sendRedirect("/listClient");}
        }catch (Exception ignored){
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ListAdminServlet doPost");

        //админ выбирает какие продукты удалить и тыкает кнопку удалить, сюда прилетают товары в виде массива
        //ну разумеется строкой их айдишники, которые потом берутся и вызывается метод Модели по удалению товара из базы данных(по айдишнику)
        try {
            if (req.getParameterValues("productForDelete") != null) {
                String[] productsID = req.getParameterValues("productForDelete");
                for (String productID : productsID) {
                    Model.getInstance().delete(productID);
                }
            }
        }catch (NullPointerException e){
            //если товаров для удаления нет, а кнопки нажимались, падает эксепшн и чекается а не добавился ли новый товар
            try{
                //в первой строке писали имя, по идее здесь можно ограничить, типа чтобы имя не было меньше 5 символов етц
                //но решил главное чтобы работала, как я ограничивал я показал в никах
                String name = req.getParameter("name");
                //цена офк в тенге, мы же патриоты
                int price = Integer.parseInt(req.getParameter("price"));
                //создаем продукт
                Product product = new Product(name, price);
                //добавляем в базу
                Model.getInstance().add(product);
                doGet(req, resp);

            }catch (Exception f){
                //если и ни того ни сего нет, либо забыли цену написать либо чо еще, короче косяк - вылетает нуллдата
                req.setAttribute("nullData", "");
                doGet(req, resp);
            }
        }
        //удалили продукт - прост обновляется вьюшка и все
        doGet(req, resp);
    }
}
