package app.model.daoFake.get;

import app.entities.products.Product;
import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetBasket extends AbstractDao {

    private List<Product> list = new ArrayList<>();

    public GetBasket(User user) {
        sql = "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.STATE  = 'NOT_ORDERED' AND  ORDERS .CUSTOMER_ID =" + user.getId();
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("NAME");
            int price = resultSet.getInt("PRICE");
            int id = resultSet.getInt("ID");
            int count = resultSet.getInt("COUNT");
            list.add(new Product(id, name, price, count));
        }
        return list;
    }
}
