package app.controller.dao;

import app.model.products.Product;

public interface ProductDaoInteface extends Dao<Product> {

    Product getProduct(int id);

}
