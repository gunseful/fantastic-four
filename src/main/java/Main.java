import app.entities.user.User;
import app.model.service.UserServiceImpl;

import java.sql.SQLException;

//import app.entities.products.Order;
//import app.entities.user.User;
//import app.model.controller.Repository;
//
public class Main {

    public static void main(String[] args) throws SQLException {
        //Тест

        UserServiceImpl us = new UserServiceImpl();
        us.removeFromBlackList(1548);

        User user = new User();
        user.setId(1548);
        System.out.println(us.checkBlackList(user));



    }

}
