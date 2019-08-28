package app.controller.service;

import app.model.products.Order;
import app.model.products.Product;
import app.model.products.ProductOrder;
import app.model.user.User;
import app.controller.dao.OrderDao;
import app.controller.dao.ProductDao;
import app.controller.dao.ProductOrderDao;
import app.controller.dao.UserDao;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao = new OrderDao();
    private ProductOrderDao productOrderDao = new ProductOrderDao();
    private ProductDao productDao = new ProductDao();
    private UserDao userDao = new UserDao();


    public void deleteProductFromBasket(User user, int productId) throws SQLException {
        int orderId = Objects.requireNonNull(orderDao.getAll().stream().filter(o -> o.getCustomerId() == user.getId() && o.getState().equals("NOT_ORDERED")).findFirst().orElse(null)).getId();
        productOrderDao.delete(Objects.requireNonNull(productOrderDao.getAll().stream().filter(p -> p.getOrderId() == orderId && p.getProductId() == productId).findFirst().orElse(null)));
    }

    public void makeOrder(User user) throws SQLException {
        Order order = orderDao.getAll().stream().filter(u -> u.getCustomerId() == user.getId() && u.getState().equals("NOT_ORDERED")).findFirst().orElse(null);
        assert order != null;
        Date date = Date.valueOf(LocalDate.now());
        order.setCreationDate(date);
        order.setState("ORDERED");
        orderDao.update(order);
    }

    public void payOrder(int id) throws SQLException {
        Order order = orderDao.getAll().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        Objects.requireNonNull(order).setState("PAID");
        orderDao.update(order);
    }

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

    public List<Order> getOrders(User user) throws SQLException {
        List<Order> list = orderDao.getAll().stream().filter(o -> !o.getState().equals("NOT_ORDERED")).collect(Collectors.toList());
        for (Order order : list) {
            order.setProducts(new ArrayList<>());
            for (ProductOrder productOrder : productOrderDao.getAll()) {
                if (order.getId() == productOrder.getOrderId()) {
                    Product product = productDao.read(productOrder.getProductId());
                    product.setCount(productOrder.getCount());
                    order.addProduct(product);
                    order.setUser(userDao.read(order.getCustomerId()));
                }
            }
        }
        if (user.isAdministrator()) {
            return list;
        } else {
            return list.stream().filter(o -> o.getCustomerId() == user.getId()).collect(Collectors.toList());
        }
    }

    public List<Product> getBasket(User user) throws SQLException {
        List<Product> list = new ArrayList<>();
        int orderId = 0;
        for (Order order : orderDao.getAll()) {
            if (order.getCustomerId() == user.getId() && order.getState().equals("NOT_ORDERED")) {
                orderId = order.getId();
            }
        }
        for (ProductOrder productOrder : productOrderDao.getAll()) {
            if (productOrder.getOrderId() == orderId) {
                Product product = productDao.read(productOrder.getProductId());
                product.setCount(productOrder.getCount());
                list.add(product);
            }
        }
        return list;
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
        Order order = orderDao.read(id);
        productOrderDao.delete(Objects.requireNonNull(productOrderDao.getAll().stream().filter(p -> p.getOrderId() == id).findFirst().orElse(null)));
        orderDao.delete(order);
    }
}
