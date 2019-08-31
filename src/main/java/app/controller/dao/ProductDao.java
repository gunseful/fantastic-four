package app.controller.dao;

import app.model.products.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class ProductDao extends AbstractDao<Product> implements ProductDaoInteface{

    @Override
    public boolean add(Product product) {
        try {
            getPreparedStatement(String.format("INSERT INTO PRODUCTS (Name, Price) Values ('%s', %d)", product.getName(), product.getPrice())).executeUpdate();
            logger.info("Product " + product.getId() + " has been added to product list");
            return true;
        } catch (SQLException e) {
            logger.error("adding product fail", e);
        }
        return false;
    }

    @Override
    public List<Product> getAll() {
        return getResultList(String.format("Select * from %s", tableName()));
    }

    @Override
    public void update(Product product) {
        try {
            getPreparedStatement(String.format("UPDATE PRODUCTS SET NAME = '%s', PRICE = %d WHERE ID = %d", product.getName(), product.getPrice(), product.getId())).executeUpdate();
            logger.info("Product " + product.getId() + " has been updated");
        } catch (SQLException e) {
            logger.error("updating product fail", e);
        }
    }

    @Override
    public void delete(Product product) {
        try {
            getPreparedStatement(String.format("DELETE FROM PRODUCTS WHERE Id = %d", product.getId())).executeUpdate();
            logger.info("Product " + product.getId() + " has been deleted from product list");
        } catch (SQLException e) {
            logger.error("removing product fail", e);
        }
    }

    @Override
    public Product getProduct(int id){
        return findById(id);
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
            logger.error("getting list of products fail",e);
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "PRODUCTS";
    }
}
