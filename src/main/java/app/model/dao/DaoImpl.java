package app.model.dao;

import app.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DaoImpl<T> implements Dao<T> {
    public static Logger logger = LogManager.getLogger();
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    public static String sql;
    public static Connection connection = null;

    public PreparedStatement start() {
        try {
            connection = connectionPool.getConnection();
            return connection.prepareStatement(sql);
        } catch (InterruptedException | SQLException e) {
            logger.info("Fail connect to database");
            logger.error(e);
            return null;
        } finally {
            try {
                connectionPool.releaseConnection(connection);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }
}
