import app.controller.dao.OrderDao;
import app.controller.service.OrderServiceImpl;
import app.controller.service.ProductServiceImpl;
import app.controller.service.UserServiceImpl;
import app.model.user.User;

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
        User user = new User();
        user.setId(2);
//        System.out.println(orderService.getOrders(user));
//
//        System.out.println(userService.getUser(2));
//        UserDao userDao = new UserDao();

        OrderDao orderDao = new OrderDao();
        System.out.println(orderDao.getBasket(3)!=null);



//        User user = new User("4","4","4");
//        TreeMap<String, String> map = new TreeMap<>();
//        map.put("ID","2");
//        map.put("NAME","'2'");
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("SELECT * FROM %s WHERE ", "USERS"));
//        for(Map.Entry<String, String> entry : map.entrySet()) {
//            sb.append(entry);
//            if(!entry.equals(map.lastEntry())){
//                sb.append(" AND ");
//            }
//        }
//        System.out.println(sb.toString());



    }

}
