package app.controller.dao;

import app.model.products.Order;
import app.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class OrderDao extends AbstractDao<Order> implements OrderDaoInterface{

    @Override
    public boolean add(Order order) {
        try {
            PreparedStatement ps = getPreparedStatement(String.format("INSERT INTO ORDERS (CUSTOMER_ID, STATE) Values (%d, 'NOT_ORDERED')", order.getCustomerId()));
            ps.executeUpdate();
            logger.info("User " + order.getCustomerId() + " has created new order");
        } catch (SQLException e) {
            logger.error("adding new order fail", e);
        }
        return true;
    }

    @Override
    public List<Order> getAll() {
        return getResultList("SELECT * FROM ORDERS");
    }

    @Override
    public void update(Order order) {
        try {
            PreparedStatement ps = getPreparedStatement(String.format("UPDATE ORDERS SET CUSTOMER_ID = %d, CREATEDAT = ?, STATE = %s WHERE ID = %d", order.getCustomerId(), order.getState(), order.getId()));
            ps.setObject(1, order.getCreationDate());
            ps.executeUpdate();
            logger.info("Order " + order.getId() + " has been updated");
        } catch (SQLException e) {
            logger.error("updating order fail", e);
        }
    }

    @Override
    public void delete(Order order) {
        try {
            getPreparedStatement(String.format("DELETE FROM ORDERS WHERE ID = %d", order.getId())).executeUpdate();
            logger.info("order " + order.getId() + " has been deleted from order list");
        } catch (SQLException e) {
            logger.error("removing order fail", e);
        }
    }

    @Override
    public List<Order> findOrders(User user) {
        if(user.isAdministrator()) {
            return findBy("STATE='ORDERED' OR STATE ='PAID'");
        }else {
            return findBy(String.format("STATE='ORDERED' OR STATE ='PAID' AND CUSTOMER_ID=%d",user.getId()));
        }
    }

    @Override
    public Order getUserBasket(User user) {
        return singleFindBy(String.format("STATE='NOT_ORDERED' AND CUSTOMER_ID=%d", user.getId())).orElse(null);
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
            logger.error("getting list of orders fail",e);
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "ORDERS";
    }
}
