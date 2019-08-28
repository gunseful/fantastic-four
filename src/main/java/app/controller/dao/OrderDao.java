package app.controller.dao;

import app.model.products.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class OrderDao extends AbstractDao<Order> {

    @Override
    public void delete(Order order) throws SQLException {
        sql = "DELETE FROM ORDERS WHERE ID = " + order.getId();
        getPreparedStatement().executeUpdate();
        logger.info("order " + order.getId() + " has been deleted from order list");
    }

    @Override
    public boolean add(Order order) throws SQLException {
        sql = "INSERT INTO ORDERS (CUSTOMER_ID, STATE) Values (?, 'NOT_ORDERED')";
        PreparedStatement ps = getPreparedStatement();
        ps.setInt(1, order.getCustomerId());
        ps.executeUpdate();
        return true;
    }

    @Override
    public List<Order> getAll() throws SQLException {
        return getResultList("SELECT * FROM ORDERS");
    }

    @Override
    public void update(Order order) throws SQLException {
        sql = "UPDATE ORDERS SET CUSTOMER_ID = ?, CREATEDAT = ?, STATE = ? WHERE ID =" + order.getId();
        PreparedStatement ps = getPreparedStatement();
        ps.setInt(1, order.getCustomerId());
        ps.setObject(2, order.getCreationDate());
        ps.setString(3, order.getState());
        ps.executeUpdate();
    }

    @Override
    protected List<Order> parseResultSet(ResultSet resultSet) {
        try {
            List<Order> result = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("ID"));
                order.setCustomerId(resultSet.getInt("CUSTOMER_ID"));
                order.setCreationDate(resultSet.getDate("CREATEDAT"));
                order.setState(resultSet.getString("STATE"));
                result.add(order);
            }
            return result;
        } catch (SQLException e) {
            logger.error(e);
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "ORDERS";
    }
}
