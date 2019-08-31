package app.controller.dao;

import app.model.products.Product;
import app.model.products.ProductOrder;

import java.util.List;

public interface ProductOrderDaoInterface extends Dao<ProductOrder> {
    List<Product> findProductsByOrderId(int orderId);
    ProductOrder getProductOrder(int orderId, int productId);
}
