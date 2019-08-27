package app.model.service;

import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.dao.OrderDao;
import app.model.dao.ProductDao;

import java.sql.SQLException;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao = new ProductDao();

    public void addNewProduct(Product product) throws SQLException {
        productDao.add(product);
    }


    public void addToBasket(User user, int productId) throws SQLException {
        int orderId;
        OrderDao orderDao = new OrderDao();
        for (Order order : orderDao.getAll()) {
            if (order.getCustomerId() == user.getId()) {
                orderId = order.getId();
            }
        }

    }
}
