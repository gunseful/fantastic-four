package app.controller.service;

import app.controller.dao.ProductDao;
import app.model.products.Product;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao = new ProductDao();

    public void deleteProduct(int id) {
        Product product = productDao.findById(id);
        productDao.delete(product);
    }

    public void addNewProduct(Product product) {
        productDao.add(product);
    }


    public List<Product> getList() {
        return productDao.getAll();
    }
}
