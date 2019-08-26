package app.model.dao.update;

import app.model.dao.abstraction.AbstractDao;

import java.sql.SQLException;

public class PayOrder extends AbstractDao {

    public PayOrder(int id) {
        sql = "UPDATE ORDERS SET STATE = 'PAID' WHERE ID = " + id;
    }

    @Override
    public void setSql() throws SQLException {
    }
}
