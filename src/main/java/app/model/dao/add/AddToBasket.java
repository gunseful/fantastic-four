package app.model.dao.add;

import app.entities.user.User;
import app.model.dao.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddToBasket extends AbstractDao {

    private User user;
    private int productId;

    public AddToBasket(User user, int productId) {
        this.user = user;
        this.productId = productId;
    }

    @Override
    public Object preparedStatment() throws SQLException {
        sql = "SELECT * FROM ORDERS WHERE CUSTOMER_ID = " + user.getId() + " AND STATE = 'NOT_ORDERED'";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            sql = "INSERT INTO ORDERS (CUSTOMER_ID, STATE) Values (?, 'NOT_ORDERED')";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        }
        sql = "INSERT INTO PRODUCTS_ORDERS (PRODUCT_ID , ORDER_ID ) VALUES(" + productId + ", (SELECT ID FROM ORDERS WHERE CUSTOMER_ID = " + user.getId() + " AND STATE = 'NOT_ORDERED'))";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        return true;
    }
}
