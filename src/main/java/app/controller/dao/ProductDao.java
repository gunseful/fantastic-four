package app.controller.dao;

import app.model.products.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao extends DaoImpl<Product> {

    @Override
    public boolean add(Product product) throws SQLException {
        sql="INSERT INTO PRODUCTS (Name, Price) Values ('" + product.getName() + "', '" + product.getPrice() + "')";
        start().executeUpdate();
        return true;
    }


    @Override
    public void delete(Product product) throws SQLException {
        sql = "DELETE FROM PRODUCTS WHERE Id = " + product.getId();
        start().executeUpdate();
    }


    @Override
    public List<Product> getAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        sql = "Select * from Products";
        ResultSet rs = start().executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("NAME");
            int price = rs.getInt("PRICE");
            list.add(new Product(id, name, price));
        }
        return list;
    }

    @Override
    public Product read(int id) throws SQLException {
        sql = "SELECT * FROM PRODUCTS WHERE ID = " + id;
        ResultSet resultSet = start().executeQuery();
        resultSet.next();
        return new Product(id, resultSet.getString("NAME"), resultSet.getInt("PRICE"));
    }

    @Override
    public void update(Product product) throws SQLException {
        sql = "UPDATE PRODUCTS SET NAME = ?, PRICE = ? WHERE ID ="+product.getId();
        PreparedStatement ps = start();
        ps.setString(1, product.getName());
        ps.setInt(2, product.getPrice());
        ps.executeUpdate();
    }


}
