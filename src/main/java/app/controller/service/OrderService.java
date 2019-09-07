package app.controller.service;

import app.model.products.Order;
import app.model.products.Product;
import app.model.user.User;

import java.util.List;

public interface OrderService {
    void deleteProductFromBasket(User user, int productId);

    void makeOrder(User user);

    void payOrder(int id);


    boolean increaseCount(User user, int productId);

    void decreaseCount(User user, int productId);


    List<Order> getOrders(User user);

    List<Product> getBasketProducts(User user);

    void addToBasket(User user, int productId);

    void deleteOrder(int id);

}
