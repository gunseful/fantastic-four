package app.controller.dao;

import app.model.products.Order;
import app.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class OrderDao extends AbstractDao<Order> implements OrderDaoInterface {

    @Override
    public boolean add(Order order) {
        update("INSERT INTO ORDERS (CUSTOMER_ID, STATE) Values (?, 'NOT_ORDERED')", preparedStatement ->
            preparedStatement.setInt(1, order.getCustomerId())
        );
        logger.info("User " + order.getCustomerId() + " has created new order");
        return true;
    }

    @Override
    public List<Order> getAll() {
        return getResultList("SELECT * FROM ORDERS");
    }

    @Override
    public void update(Order order) {
        update("UPDATE ORDERS SET CUSTOMER_ID = ?, CREATEDAT = ?, STATE = ? WHERE ID = ?", preparedStatement -> {
            preparedStatement.setInt(1, order.getCustomerId());
            preparedStatement.setObject(2, order.getCreationDate());
            preparedStatement.setString(3, order.getState());
            preparedStatement.setInt(4, order.getId());
        });
        logger.info("Order " + order.getId() + " has been updated");
    }


    @Override
    public void delete(Order order) {
        update("DELETE FROM ORDERS WHERE ID = ?", preparedStatement ->
                preparedStatement.setInt(1, order.getId()));
        logger.info("order " + order.getId() + " has been deleted from order list");
    }

    @Override
    public List<Order> findOrders(User user) {
        if (user.isAdministrator()) {
            return getResultList("SELECT * FROM ORDERS WHERE STATE = ? OR STATE = ?", preparedStatement -> {
                preparedStatement.setString(1, "ORDERED");
                preparedStatement.setString(2, "PAID");
            });
        } else {
            return getResultList("SELECT * FROM ORDERS WHERE CUSTOMER_ID = ? AND STATE = ? OR STATE = ?", preparedStatement -> {
                preparedStatement.setString(2, "ORDERED");
                preparedStatement.setString(3, "PAID");
                preparedStatement.setInt(1, user.getId());
            });
        }
    }

    @Override
    public Order getUserBasket(User user) {
        return getSingleResult("SELECT * FROM ORDERS WHERE CUSTOMER_ID= ? AND STATE = ? ", preparedStatement -> {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, "NOT_ORDERED");
        }).orElse(null);
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
            logger.error("getting list of orders fail", e);
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "ORDERS";
    }
}
