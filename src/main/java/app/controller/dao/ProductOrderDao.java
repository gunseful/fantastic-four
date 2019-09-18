package app.controller.dao;

import app.model.products.Product;
import app.model.products.ProductOrder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class ProductOrderDao extends AbstractDao<ProductOrder> implements ProductOrderDaoInterface {

    private ProductDao productDao = new ProductDao();

    @Override
    public boolean add(ProductOrder productOrder) {
            saveOrUpdate("INSERT INTO PRODUCTS_ORDERS (PRODUCT_ID, ORDER_ID, COUNT) Values (?,?,?)", preparedStatement -> {
                preparedStatement.setInt(1, productOrder.getProductId());
                preparedStatement.setInt(2, productOrder.getOrderId());
                preparedStatement.setInt(3, productOrder.getCount());
            });
            logger.info("product id={} has been added to order id={}", productOrder.getProductId(), productOrder.getOrderId());
            return true;
    }

    @Override
    public List<ProductOrder> getAll() {
        return getResultList("SELECT * FROM PRODUCTS_ORDERS");
    }

    @Override
    public void update(ProductOrder productOrder) {
            saveOrUpdate("UPDATE PRODUCTS_ORDERS SET COUNT = ? WHERE PRODUCT_ID = ? AND ORDER_ID = ?", preparedStatement -> {
                preparedStatement.setInt(1, productOrder.getCount());
                preparedStatement.setInt(2, productOrder.getProductId());
                preparedStatement.setInt(3, productOrder.getOrderId());
            });
            logger.info("update product id={} count({}) in order id={}", productOrder.getProductId(), productOrder.getCount(), productOrder.getOrderId());
    }

    @Override
    public void delete(ProductOrder productOrder) {
            saveOrUpdate("DELETE FROM PRODUCTS_ORDERS WHERE PRODUCT_ID = ? AND ORDER_ID = ?", preparedStatement -> {
                preparedStatement.setInt(1, productOrder.getProductId());
                preparedStatement.setInt(2, productOrder.getOrderId());

            });
            logger.info("product id={} has been deleted from order id={}", productOrder.getProductId(),  productOrder.getOrderId());
        }

    @Override
    public ProductOrder getProductOrder(int orderId, int productId) {
        return getSingleResult("SELECT * FROM PRODUCTS_ORDERS WHERE ORDER_ID = ? AND PRODUCT_ID=?", preparedStatement -> {
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, productId);
        }).orElse(null);
    }

    @Override
    public List<Product> findProductsByOrderId(int orderId) {
        List<Product> products = new ArrayList<>();
        List<ProductOrder> list = getResultList("SELECT * FROM PRODUCTS_ORDERS WHERE ORDER_ID = ?", preparedStatement ->
            preparedStatement.setInt(1, orderId));
            for (ProductOrder productOrder : list) {
            Product product = productDao.getProduct(productOrder.getProductId());
            product.setCount(productOrder.getCount());
            products.add(product);
        }
        return products;
    }

    @Override
    protected List<ProductOrder> parseResultSet(ResultSet resultSet) {
        try {
            List<ProductOrder> list = new ArrayList<>();
            while (resultSet.next()) {
                ProductOrder productOrder = new ProductOrder();
                productOrder.setProductId(resultSet.getInt("PRODUCT_ID"));
                productOrder.setOrderId(resultSet.getInt("ORDER_ID"));
                productOrder.setCount(resultSet.getInt("COUNT"));
                list.add(productOrder);
            }
            return list;
        } catch (SQLException e) {
            logger.error("getting list of order's products  fail", e);
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "PRODUCTS_ORDERS";
    }
}
