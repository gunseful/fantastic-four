package app.model.dao.delete;

import app.model.dao.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteOrder extends AbstractDao {
    private int id;

    public DeleteOrder(int id) {
        this.id = id;
    }

    @Override
    public Object preparedStatment() throws SQLException {
        connection.setAutoCommit(false);
        String sqlProductOrders = "DELETE FROM PRODUCTS_ORDERS  WHERE ORDER_ID = " + id;
        PreparedStatement preparedStatementPr = connection.prepareStatement(sqlProductOrders);
        preparedStatementPr.executeUpdate();
        String sqlOrders = "DELETE FROM ORDERS WHERE ID = " + id;
        PreparedStatement preparedStatementO = connection.prepareStatement(sqlOrders);
        preparedStatementO.executeUpdate();
        connection.commit();
        connection.setAutoCommit(true);
        logger.info("product " + id + " has been deleted from order list");
        return null;
    }
}
