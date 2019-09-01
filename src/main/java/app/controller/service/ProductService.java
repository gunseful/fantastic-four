package app.controller.service;

import app.model.products.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    void deleteProduct(int id) throws SQLException;
    void addNewProduct(String name, int price) throws SQLException;
    List<Product> getList() throws SQLException;

    }
