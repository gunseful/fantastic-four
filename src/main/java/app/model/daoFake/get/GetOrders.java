package app.model.daoFake.get;

import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetOrders extends AbstractDao {


    public GetOrders(User user) {
        sql = (!user.isAdministrator()) ? "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.CUSTOMER_ID=2 AND ORDERS.STATE = 'ORDERED' OR STATE ='PAID' " :
                "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.STATE = 'ORDERED' OR STATE ='PAID'";
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        int orderId = 0;
        List<Order> list = new ArrayList<>();
        while (resultSet.next()) {
            if (!containsOrder(list, resultSet.getInt("ORDER_ID"))) {
                orderId = resultSet.getInt("ORDER_ID");
                java.sql.Date dbSqlDate = resultSet.getDate("CREATEDAT");
                int customerId = resultSet.getInt("CUSTOMER_ID");
                boolean isPaid = resultSet.getString("STATE").equals("PAID");
                List<Product> products = new ArrayList<>();
                list.add(new Order(orderId, dbSqlDate, products, isPaid, (User)new GetUser(customerId).start()));
            }
            for (Order order : list) {
                if (resultSet.getInt("ORDER_ID") == orderId) {
                    order.addProduct(new Product(resultSet.getInt("PRODUCT_ID"), resultSet.getString("NAME"), resultSet.getInt("PRICE"), resultSet.getInt("COUNT")));
                }
            }
        }
        return list;
    }
    private synchronized boolean containsOrder(final List<Order> list, final int id) {
        return list.stream().anyMatch(o -> o.getId() == id);
    }
}
