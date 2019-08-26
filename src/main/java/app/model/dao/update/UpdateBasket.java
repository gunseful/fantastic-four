package app.model.dao.update;

import app.entities.user.User;
import app.model.dao.abstraction.AbstractDao;
import app.model.dao.delete.DeleteProductFromBasket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateBasket extends AbstractDao {

    private User user;
    private int productId;

    public UpdateBasket(boolean add, User user, int productId) {
        sql = (add) ? "update PRODUCTS_ORDERS PR set PR.COUNT = PR.COUNT+1 where exists (select * from ORDERS O where PR.ORDER_ID  = O.ID AND O.CUSTOMER_ID = " + user.getId() + " AND O.STATE='NOT_ORDERED' AND PR.PRODUCT_ID=" + productId + ")"
                : "update PRODUCTS_ORDERS PR set PR.COUNT = PR.COUNT-1 where exists (select * from ORDERS O where PR.ORDER_ID  = O.ID AND O.CUSTOMER_ID = " + user.getId() + " AND O.STATE='NOT_ORDERED' AND PR.PRODUCT_ID=" + productId + ")";
        this.user = user;
        this.productId = productId;
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        String sql = "SELECT * FROM PRODUCTS_ORDERS inner join orders on PRODUCTS_ORDERS.ORDER_ID = ORDERS.ID where PRODUCTS_ORDERS.PRODUCT_ID = ? AND ORDERS.CUSTOMER_ID = ? AND ORDERS.STATE = 'NOT_ORDERED'";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, productId);
        preparedStatement.setInt(2, user.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if (resultSet.getInt("COUNT") == 0) {
            new DeleteProductFromBasket(user, productId).start();
        }
        return null;
    }
}
