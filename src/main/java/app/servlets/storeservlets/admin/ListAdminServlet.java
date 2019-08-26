package app.servlets.storeservlets.admin;

import app.entities.products.Product;
import app.entities.user.User;
import app.model.dao.add.AddProduct;
import app.model.dao.delete.DeleteProduct;
import app.model.dao.get.GetList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ResourceBundle;

public class ListAdminServlet extends HttpServlet {
    public static Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        //как всегда берем юзера из сесси для работы с ним
        User user = (User) req.getSession().getAttribute("user");
        //кидаем все продукты из базы данных аттрибутом
//        Repository uc = new Repository();
        req.setAttribute("products", new GetList().start());
        //проверяем не залез ли шпион и переводим на страницу клиента если залез все таки
        try {
            if (user.isAdministrator()) {
                logger.info("User=" + user.getNickname() + "requests product list");
                RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/admin/listAdmin.jsp");
                requestDispatcher.forward(req, resp);
            } else {
                resp.sendRedirect("/listClient");
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        User user = (User) req.getSession().getAttribute("user");
        //админ выбирает какие продукты удалить и тыкает кнопку удалить, сюда прилетают товары в виде массива
        //ну разумеется строкой их айдишники, которые потом берутся и вызывается метод Модели по удалению товара из базы данных(по айдишнику)
        try {
            if (req.getParameterValues("productForDelete") != null) {
                String[] productsID = req.getParameterValues("productForDelete");
                for (String productID : productsID) {
//                    Repository controller = (Repository) req.getSession().getAttribute("controller");
//                    controller.deleteProduct(productID);
                    new DeleteProduct(productID).start();
                    logger.info("User=" + user.getNickname() + " has been deleted " + productID);
                }
            } else {
                //в первой строке писали имя, по идее здесь можно ограничить, типа чтобы имя не было меньше 5 символов етц
                //но решил главное чтобы работала, как я ограничивал я показал в никах
                String name = req.getParameter("name");
                //цена офк в тенге, мы же патриоты
                ResourceBundle bundle = (ResourceBundle) req.getSession().getAttribute("bundle");
                Double d = (Double) bundle.getObject("exchange.rates");
                int price = (int) (Integer.parseInt(req.getParameter("price")) / d);
                //создаем продукт
                Product product = new Product(name, price);
                //добавляем в базу
//                Repository controller = (Repository) req.getSession().getAttribute("controller");
//                controller.add(product);
                new AddProduct(product).start();
                doGet(req, resp);
                logger.info("User=" + user.getNickname() + " has been added new product - " + product.getName());

            }
        } catch (NullPointerException e) {
            logger.error("User=" + user.getNickname() + " was failed");
            //если и ни того ни сего нет, либо забыли цену написать либо чо еще, короче косяк - вылетает нуллдата
            req.setAttribute("nullData", "");
            doGet(req, resp);
        }
        //удалил или добавил продукт - прост обновляется вьюшка и все
        doGet(req, resp);
    }
}
