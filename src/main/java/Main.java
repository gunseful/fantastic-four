import app.controller.dao.OrderDao;
import app.controller.service.OrderServiceImpl;
import app.model.user.User;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {

    public static void main(String[] args) {
        //Тест

//        UserServiceImpl userService = new UserServiceImpl();
        OrderServiceImpl orderService = new OrderServiceImpl();
//        ProductServiceImpl productService = new ProductServiceImpl();
        User user = new User();
        user.setId(1616);
        OrderDao orderDao = new OrderDao();
        System.out.println(orderService.getBasketProducts(user));




    }

}
