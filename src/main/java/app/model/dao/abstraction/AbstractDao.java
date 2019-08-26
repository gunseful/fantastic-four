package app.model.dao.abstraction;

import app.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDao {
    public static Logger logger = LogManager.getLogger();
    public ConnectionPool connectionPool = ConnectionPool.getInstance();
    public static String sql;
    public static Connection connection = null;

    public Object start() {
        try {
            connection = connectionPool.getConnection();
            return preparedStatment();
        } catch (InterruptedException | SQLException e) {
            logger.info("Fail connect to database");
            logger.error(e);
            return false;
        } finally {
            try {
                connectionPool.releaseConnection(connection);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
    }

    public Object preparedStatment() throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        return null;}
}
