package app.controller.dao;

import app.model.products.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao extends DaoImpl<Order> {

    @Override
    public void delete(Order order) throws SQLException {
        sql = "DELETE FROM ORDERS WHERE ID = " + order.getId();
        start().executeUpdate();
        logger.info("order " + order.getId() + " has been deleted from order list");
    }

    @Override
    public boolean add(Order order) throws SQLException {
        sql = "INSERT INTO ORDERS (CUSTOMER_ID, STATE) Values (?, 'NOT_ORDERED')";
        PreparedStatement ps = start();
        ps.setInt(1, order.getCustomerId());
        ps.executeUpdate();
        return true;
    }

    @Override
    public Order read(int id) throws SQLException {
        sql = "SELECT * FROM ORDERS WHERE ID =" + id;
        ResultSet resultSet = start().executeQuery();
        resultSet.next();
        Order order = new Order();
        order.setId(resultSet.getInt("ID"));
        order.setCustomerId(resultSet.getInt("CUSTOMER_ID"));
        order.setCreationDate(resultSet.getDate("CREATEDAT"));
        order.setState(resultSet.getString("STATE"));
        return order;
    }

    @Override
    public List<Order> getAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        sql = "SELECT * FROM ORDERS";
        ResultSet resultSet = start().executeQuery();
        while (resultSet.next()) {
            Order order = new Order();
            order.setId(resultSet.getInt("ID"));
            order.setCustomerId(resultSet.getInt("CUSTOMER_ID"));
            order.setCreationDate(resultSet.getDate("CREATEDAT"));
            order.setState(resultSet.getString("STATE"));
            list.add(order);
        }
        return list;
    }

    @Override
    public void update(Order order) throws SQLException {
        sql = "UPDATE ORDERS SET CUSTOMER_ID = ?, CREATEDAT = ?, STATE = ? WHERE ID =" + order.getId();
        PreparedStatement ps = start();
        ps.setInt(1, order.getCustomerId());
        ps.setObject(2, order.getCreationDate());
        ps.setString(3, order.getState());
        ps.executeUpdate();
    }


}
