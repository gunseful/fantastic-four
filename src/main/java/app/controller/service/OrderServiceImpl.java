package app.controller.service;

import app.controller.dao.OrderDao;
import app.controller.dao.ProductOrderDao;
import app.controller.dao.UserDao;
import app.model.products.Order;
import app.model.products.Product;
import app.model.products.ProductOrder;
import app.model.user.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private OrderDao orderDao = new OrderDao();
    private ProductOrderDao productOrderDao = new ProductOrderDao();
    private UserDao userDao = new UserDao();


    @Override
    public void deleteProductFromBasket(User user, int productId) {
        ProductOrder productOrder = new ProductOrder(orderDao.getUserBasket(user).getId(), productId);
        productOrderDao.delete(productOrder);
    }

    @Override
    public void makeOrder(User user) {
        Order order = orderDao.getUserBasket(user);
        order.setCreationDate(Date.valueOf(LocalDate.now()));
        order.setState("'ORDERED'");
        orderDao.update(order);
    }

    @Override
    public void payOrder(int id) {
        Order order = orderDao.findById(id);
        order.setState("'PAID'");
        orderDao.update(order);
    }

    @Override
    public boolean updateBasket(boolean add, User user, int productId) {
        Order order = orderDao.getUserBasket(user);
        if (order != null) {
            ProductOrder productOrder = productOrderDao.getProductOrder(order.getId(), productId);
            if (productOrder != null) {
                int count = productOrder.getCount();
                count = (add) ? count + 1 : count - 1;
                if (count == 0) {
                    productOrderDao.delete(productOrder);
                    return true;
                } else {
                    productOrder.setCount(count);
                    productOrderDao.update(productOrder);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Order> getOrders(User user) {
        List<Order> orders = orderDao.findOrders(user);
        for (Order order : orders) {
            int customerId = order.getCustomerId();
            order.setUser(userDao.findById(customerId));
            order.setProducts(productOrderDao.findProductsByOrderId(order.getId()));
        }
        return orders;
    }

    @Override
    public List<Product> getBasketProducts(User user) {
        Order order = orderDao.getUserBasket(user);
        if (order != null) {
            return productOrderDao.findProductsByOrderId(order.getId());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void addToBasket(User user, int productId) {
        Order order = orderDao.getUserBasket(user);
        if (order == null) {
            Order newOrder = new Order();
            newOrder.setCustomerId(user.getId());
            orderDao.add(newOrder);
        }
        order = orderDao.getUserBasket(user);
        ProductOrder productOrder = new ProductOrder();
        productOrder.setCount(1);
        productOrder.setProductId(productId);
        productOrder.setOrderId(order.getId());
        productOrderDao.add(productOrder);
    }

    @Override
    public void deleteOrder(int id) {
        Order order = orderDao.findById(id);
        for (Product product : productOrderDao.findProductsByOrderId(id)) {
            ProductOrder productOrder = new ProductOrder(id, product.getId());
            productOrderDao.delete(productOrder);
        }
        orderDao.delete(order);
    }
}
