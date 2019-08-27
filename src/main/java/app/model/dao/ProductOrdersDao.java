package app.model.dao;

import app.entities.products.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductOrdersDao extends DaoImpl<Product> {

    @Override
    public boolean add(Product product) throws SQLException {
        return false;
    }

    @Override
    public Product read(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Product> getAll() throws SQLException {
        return null;
    }

    @Override
    public void update(Product product) throws SQLException {

    }

    @Override
    public void delete(int id) throws SQLException {

    }
}
