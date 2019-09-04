package app.controller.dao;

import app.model.products.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class ProductDao extends AbstractDao<Product> implements ProductDaoInteface {

    @Override
    public boolean add(Product product) {
        saveOrUpdate("INSERT INTO PRODUCTS (Name, Price) Values (?, ?)", preparedStatement -> {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
        });
        logger.info("Product " + product.getId() + " has been added to product list");
        return true;
    }

    @Override
    public List<Product> getAll() {
        return getResultList(String.format("Select * from %s", tableName()));
    }

    @Override
    public void update(Product product) {
        saveOrUpdate("UPDATE PRODUCTS SET NAME = ?, PRICE = ? WHERE ID = ?", preparedStatement -> {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.setInt(3, product.getId());
        });
        logger.info("Product " + product.getId() + " has been updated");
    }

    @Override
    public void delete(Product product) {
        saveOrUpdate("DELETE FROM PRODUCTS WHERE Id = ?", preparedStatement ->
            preparedStatement.setInt(1, product.getId()));
        logger.info("Product " + product.getId() + " has been deleted from product list");
    }

    @Override
    public Product getProduct(int id) {
        return findById(id).orElseThrow(IllegalStateException::new);
    }

    @Override
    protected List<Product> parseResultSet(ResultSet resultSet) {
        try {
            List<Product> list = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int price = resultSet.getInt("PRICE");
                list.add(new Product(id, name, price));
            }
            return list;
        } catch (SQLException e) {
            logger.error("getting list of products fail", e);
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "PRODUCTS";
    }
}
