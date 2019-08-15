package app.model.controller;

import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractController {
    private Connection connection;
    private ConnectionPool connectionPool;

    public AbstractController() {
        connectionPool = ConnectionPool.getInstance();
        connection = connectionPool.getConnection();
    }

    // Возвращения конекшена в пул
    public void returnConnectionInPool() {
        connectionPool.returnConnection(connection);
    }

    // Получение экземпляра PrepareStatement
    public PreparedStatement getPrepareStatement(String sql) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    // Закрытие PrepareStatement
    public void closePrepareStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


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
