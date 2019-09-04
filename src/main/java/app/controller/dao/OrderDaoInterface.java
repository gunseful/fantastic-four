package app.controller.dao;

import app.model.products.Order;
import app.model.user.User;

import java.util.List;
import java.util.Optional;

public interface OrderDaoInterface extends Dao<Order> {

    List<Order> findOrders(User user);

    Optional<Order> getUserBasket(User user);

}
