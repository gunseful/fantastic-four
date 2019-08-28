import app.model.products.Product;
import app.controller.service.OrderServiceImpl;
import app.controller.service.ProductServiceImpl;
import app.controller.service.UserServiceImpl;

import java.sql.SQLException;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {

    public static void main(String[] args) throws SQLException {
        //Тест

        UserServiceImpl userService = new UserServiceImpl();
        OrderServiceImpl orderService = new OrderServiceImpl();
        ProductServiceImpl productService = new ProductServiceImpl();
        Product product = new Product();
//        product.setName("12");
//        product.setPrice(123);
        productService.deleteProduct(1015);

//        orderService.getOrders(user).forEach(System.out::println);

//        System.out.println(userService.getUserByNickname("2"));


//        orderService.deleteOrder(871);


    }

}
