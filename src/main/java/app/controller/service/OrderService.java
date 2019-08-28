package app.controller.service;

import app.model.products.Order;
import app.model.products.Product;
import app.model.user.User;

import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    void deleteProductFromBasket(User user, int productId) throws SQLException;

    void makeOrder(User user) throws SQLException;

    void payOrder(int id) throws SQLException;

    boolean updateBasket(boolean add, User user, int productId) throws SQLException;

    List<Order> getOrders(User user) throws SQLException;

    List<Product> getBasket(User user) throws SQLException;

    void addToBasket(User user, int productId) throws SQLException;

    void deleteOrder(int id) throws SQLException;

}
