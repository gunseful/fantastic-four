package app.controller.service;

import app.controller.dao.OrderDao;
import app.controller.dao.ProductDao;
import app.controller.dao.ProductOrderDao;
import app.controller.dao.UserDao;
import app.model.products.Order;
import app.model.products.Product;
import app.model.products.ProductOrder;
import app.model.user.User;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao = new OrderDao();
    private ProductOrderDao productOrderDao = new ProductOrderDao();
    private ProductDao productDao = new ProductDao();
    private UserDao userDao = new UserDao();

    //todo ik все заказы перебирать не очень, за менить на findby
    public void deleteProductFromBasket(User user, int productId) throws SQLException {
        int orderId = Objects.requireNonNull(orderDao.getAll().stream().filter(o -> o.getCustomerId() == user.getId() && o.getState().equals("NOT_ORDERED")).findFirst().orElse(null)).getId();
        productOrderDao.delete(Objects.requireNonNull(productOrderDao.getAll().stream().filter(p -> p.getOrderId() == orderId && p.getProductId() == productId).findFirst().orElse(null)));
    }

    //todo ik все заказы перебирать не очень, за менить на findby
    public void makeOrder(User user) {
        Order order = orderDao.getAll().stream().filter(u -> u.getCustomerId() == user.getId() && u.getState().equals("NOT_ORDERED")).findFirst().orElse(null);
        assert order != null;
        Date date = Date.valueOf(LocalDate.now());
        order.setCreationDate(date);
        order.setState("'ORDERED'");
        orderDao.update(order);
    }

    //todo ik перебирает все заказы, заменить на findby
    public void payOrder(int id) {
        orderDao.findById(id);
        Order order = orderDao.getAll().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        Objects.requireNonNull(order).setState("PAID");
        orderDao.update(order);
    }

    //todo ik перебирает все заказы, заменить на findby
    public boolean updateBasket(boolean add, User user, int productId) throws SQLException {
        Order order = orderDao.getAll().stream().filter(o -> o.getCustomerId() == user.getId() && o.getState().equals("NOT_ORDERED")).findFirst().orElse(null);
        ProductOrder productOrder = productOrderDao.getAll().stream().filter(o -> {
            if (order != null) {
                return o.getOrderId() == order.getId() && o.getProductId() == productId;
            } else {
                return false;
            }
        }).findFirst().orElse(null);
        if (add) {
            if (productOrder != null) {
                productOrder.setCount(productOrder.getCount() + 1);
            } else {
                return false;
            }
        } else {
            Objects.requireNonNull(productOrder).setCount(productOrder.getCount() - 1);
        }
        productOrderDao.update(productOrder);
        ProductOrder productOrderFinal = productOrderDao.getAll().stream().filter(o -> o.getOrderId() == order.getId() && o.getProductId() == productId).findFirst().orElse(null);
        if (Objects.requireNonNull(productOrderFinal).getCount() == 0) {
            productOrderDao.delete(productOrderFinal);
        }
        return true;

    }

    //todo ik done
    public List<Order> getOrders(User user) {
        List<Order> list = orderDao.findOrders();
        for (Order order : list) {
            order.setProducts(new ArrayList<>(productOrderDao.findProductsByOrderId(order.getId())));
            order.setUser(userDao.findById(order.getCustomerId()));
        }
        if (user.isAdministrator()) {
            return list;
        } else {
            return list.stream().filter(o -> o.getCustomerId() == user.getId()).collect(Collectors.toList());
        }
    }

    //todo возможно стоит вынести отдельно в дао вывод корзины
    public List<Product> getBasket(User user) {
        Order order = orderDao.singleFindBy(String.format("CUSTOMER_ID= %d AND STATE = 'NOT_ORDERED'", user.getId())).orElse(null);
        if (order != null) {
            return productOrderDao.findProductsByOrderId(order.getId());
        }
        return null;
    }


    public void addToBasket(User user, int productId) throws SQLException {
        int orderId = 0;
        for (Order order : orderDao.getAll()) {
            if (order.getCustomerId() == user.getId() && order.getState().equals("NOT_ORDERED")) {
                orderId = order.getId();
            }
        }
        if (orderId == 0) {
            Order order = new Order();
            order.setCustomerId(user.getId());
            orderDao.add(order);
        }
        ProductOrder productOrder = new ProductOrder();
        productOrder.setCount(1);
        productOrder.setProductId(productId);
        productOrder.setOrderId(orderId);
        productOrderDao.add(productOrder);
    }

    public void deleteOrder(int id) throws SQLException {
        Order order = orderDao.findById(id);
        productOrderDao.delete(Objects.requireNonNull(productOrderDao.getAll().stream().filter(p -> p.getOrderId() == id).findFirst().orElse(null)));
        orderDao.delete(order);
    }
}
