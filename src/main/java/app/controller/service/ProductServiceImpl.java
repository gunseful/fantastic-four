package app.controller.service;

import app.model.products.Product;
import app.controller.dao.ProductDao;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao = new ProductDao();

    public void deleteProduct(int id) throws SQLException {
        Product product = productDao.read(id);
        productDao.delete(product);
    }

    public void addNewProduct(Product product) throws SQLException {
        productDao.add(product);
    }


    public List<Product> getList() throws SQLException {
        return productDao.getAll();
    }
}
