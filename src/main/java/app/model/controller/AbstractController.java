package app.model.controller;

import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.pool.ConnectionPool;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractController {
    private ConnectionPool connectionPool;
    private DataSource dataSource;

    public AbstractController() {
        connectionPool = new ConnectionPool();
        dataSource = ConnectionPool.getDataSource();
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    //    // Получение экземпляра PrepareStatement
//    public PreparedStatement getPrepareStatement(String sql) {
//        PreparedStatement ps = null;
//        try {
//            ps = connection.prepareStatement(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ps;
//    }
//
//    // Закрытие PrepareStatement
//    public void closePrepareStatement(PreparedStatement ps) {
//        if (ps != null) {
//            try {
//                ps.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public abstract void deleteProduct(String id);
    public abstract void add(Product product);
    public abstract User getUser(int id);
    public abstract User getUserByNickName(String nickname);
    public abstract void addToBasket(User user, int id);
    public abstract boolean addNewUser(User user);
    public abstract boolean checkLogginAndPassword(User user);
    public abstract void addUserToBlackList(User user);
    public abstract void makeOrder(User user);
    public abstract void payOrder(int id);
    public abstract List<Order> getOrders(User user);
    public abstract void deleteOrder(int id);
    public abstract List<Product> getList();
    public abstract boolean checkBlackList(User user);
    public abstract List<User> getBlackList();
    public abstract void deleteFromBlackList(int id);
    public abstract void deleteProductFromBasket (String id);
    public abstract List<Product> getBasketList (User user);
}
