package app.model.dao;

import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.daoFake.delete.DeleteProductFromBasket;
import app.model.daoFake.get.GetUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao extends DaoImpl<Order> {

    @Override
    public void delete(int id) throws SQLException {
        sql = "DELETE FROM PRODUCTS_ORDERS  WHERE ORDER_ID = " + id;
        start().executeUpdate();
        sql = "DELETE FROM ORDERS WHERE ID = " + id;
        start().executeUpdate();
        logger.info("product " + id + " has been deleted from order list");
    }

    @Override
    public boolean add(Order order) throws SQLException {
        sql = "INSERT INTO ORDERS (CUSTOMER_ID, STATE) Values (?, 'NOT_ORDERED')";
        PreparedStatement ps = start();
        ps.setInt(1, order.getCustomerId());
        ps.executeUpdate();
        return true;
        }

    @Override
    public Order read(int id) throws SQLException {

        return null;
    }

    @Override
    public List<Order> getAll() throws SQLException {return null;}

    @Override
    public void update(Order order) throws SQLException {

    }

    public void deleteOrder(int id) throws SQLException {
        String sqlProductOrders = "DELETE FROM PRODUCTS_ORDERS  WHERE ORDER_ID = " + id;
        start().executeUpdate();
        String sqlOrders = "DELETE FROM ORDERS WHERE ID = " + id;
        start().executeUpdate();
        logger.info("product " + id + " has been deleted from order list");
    }

    public List<Order> getOrders(User user) throws SQLException {
        sql = (!user.isAdministrator()) ? "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.CUSTOMER_ID=2 AND ORDERS.STATE = 'ORDERED' OR STATE ='PAID' " :
                "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.STATE = 'ORDERED' OR STATE ='PAID'";
        ResultSet resultSet = start().executeQuery();
        int orderId = 0;
        List<Order> list = new ArrayList<>();
        while (resultSet.next()) {
            if (!containsOrder(list, resultSet.getInt("ORDER_ID"))) {
                orderId = resultSet.getInt("ORDER_ID");
                java.sql.Date dbSqlDate = resultSet.getDate("CREATEDAT");
                int customerId = resultSet.getInt("CUSTOMER_ID");
                boolean isPaid = resultSet.getString("STATE").equals("PAID");
                List<Product> products = new ArrayList<>();
                list.add(new Order(orderId, dbSqlDate, products, isPaid, (User) new GetUser(customerId).start()));
            }
            for (Order order : list) {
                if (resultSet.getInt("ORDER_ID") == orderId) {
                    order.addProduct(new Product(resultSet.getInt("PRODUCT_ID"), resultSet.getString("NAME"), resultSet.getInt("PRICE"), resultSet.getInt("COUNT")));
                }
            }
        }
        return list;
    }

    public void payOrder(int id) throws SQLException {
        sql = "UPDATE ORDERS SET STATE = 'PAID' WHERE ID = " + id;
        start().executeUpdate();
    }

    public void updateBasket(boolean add, User user, int productId) throws SQLException {
        sql = (add) ? "update PRODUCTS_ORDERS PR set PR.COUNT = PR.COUNT+1 where exists (select * from ORDERS O where PR.ORDER_ID  = O.ID AND O.CUSTOMER_ID = " + user.getId() + " AND O.STATE='NOT_ORDERED' AND PR.PRODUCT_ID=" + productId + ")"
                : "update PRODUCTS_ORDERS PR set PR.COUNT = PR.COUNT-1 where exists (select * from ORDERS O where PR.ORDER_ID  = O.ID AND O.CUSTOMER_ID = " + user.getId() + " AND O.STATE='NOT_ORDERED' AND PR.PRODUCT_ID=" + productId + ")";
        start().executeUpdate();
        String sql = "SELECT * FROM PRODUCTS_ORDERS inner join orders on PRODUCTS_ORDERS.ORDER_ID = ORDERS.ID where PRODUCTS_ORDERS.PRODUCT_ID = ? AND ORDERS.CUSTOMER_ID = ? AND ORDERS.STATE = 'NOT_ORDERED'";
        start().setInt(1, productId);
        start().setInt(2, user.getId());
        ResultSet resultSet = start().executeQuery();
        resultSet.next();
        if (resultSet.getInt("COUNT") == 0) {
            new DeleteProductFromBasket(user, productId).start();
        }
    }




    private synchronized boolean containsOrder(final List<Order> list, final int id) {
        return list.stream().anyMatch(o -> o.getId() == id);
    }




}
