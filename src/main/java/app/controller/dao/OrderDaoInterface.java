package app.controller.dao;

import app.model.products.Order;
import app.model.user.User;

import java.util.List;

public interface OrderDaoInterface extends Dao<Order> {

    List<Order> findOrders(User user);

    Order getUserBasket(User user);

}
