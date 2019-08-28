package app.controller.dao;

import app.model.products.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class ProductDao extends AbstractDao<Product> {

    @Override
    public boolean add(Product product) throws SQLException {
        sql="INSERT INTO PRODUCTS (Name, Price) Values ('" + product.getName() + "', '" + product.getPrice() + "')";
        getPreparedStatement().executeUpdate();
        return true;
    }


    @Override
    public void delete(Product product) throws SQLException {
        sql = "DELETE FROM PRODUCTS WHERE Id = " + product.getId();
        getPreparedStatement().executeUpdate();
    }


    @Override
    public List<Product> getAll() throws SQLException {
        return getResultList("Select * from Products");
    }

    @Override
    public void update(Product product) throws SQLException {
        sql = "UPDATE PRODUCTS SET NAME = ?, PRICE = ? WHERE ID =" + product.getId();
        PreparedStatement ps = getPreparedStatement();
        ps.setString(1, product.getName());
        ps.setInt(2, product.getPrice());
        ps.executeUpdate();
    }

    @Override
    protected List<Product> parseResultSet(ResultSet resultSet) {
        try {
            List<Product> list = new ArrayList<>();
            ResultSet rs = getPreparedStatement().executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int price = rs.getInt("PRICE");
                list.add(new Product(id, name, price));
            }
            return list;
        } catch (SQLException e) {
            logger.error(e);
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "PRODUCTS";
    }
}
