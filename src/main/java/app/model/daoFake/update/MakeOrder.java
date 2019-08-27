package app.model.daoFake.update;

import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class MakeOrder extends AbstractDao {

    private LocalDate creationDate;

    public MakeOrder(User user) {
        creationDate = LocalDate.now();
        sql = "UPDATE ORDERS SET STATE = 'ORDERED', CREATEDAT=? WHERE CUSTOMER_ID = " + user.getId() + " AND STATE = 'NOT_ORDERED'";
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, creationDate);
        preparedStatement.executeUpdate();
        return null;
    }
}
