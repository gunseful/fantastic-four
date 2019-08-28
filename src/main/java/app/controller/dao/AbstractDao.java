package app.controller.dao;

import app.controller.pool.ConnectionPool;
import app.exception.NonUniqueResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> implements Dao<T> {

    public static Logger logger = LogManager.getLogger(AbstractDao.class);
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    public static String sql;
    public static Connection connection = null;

    public PreparedStatement getPreparedStatement() {
        try {
            connection = connectionPool.getConnection();
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            logger.info("Fail connect to database");
            logger.error(e);
            //todo lol
            return null;
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    //todo make it Optional<T>
    @Override
    public T findById(int id) {
        return getSingleResult((String.format("SELECT * FROM %s WHERE ID = %d", tableName(), id))).orElseThrow(IllegalStateException::new);
    }

    /**
     * Returns Optional with result. Or empty if nothing found.
     * Throws exception if there are multiple results.
     */
    protected Optional<T> getSingleResult(String sql) {
        final List<T> resultList = getResultList(sql);
        if (resultList.size() > 1) {
            throw new NonUniqueResultException("Non single result. Found - " + resultList.size());
        }
        return resultList.stream().findFirst();
    }

    /**
     * You should only return connection when you're finished all the job with it
     */
    protected List<T> getResultList(String sql) {
        final Connection connection = connectionPool.getConnection();
        try {
            final ResultSet resultSet = connection.prepareStatement(sql).getResultSet();
            //Structural pattern - `Template method`
            return parseResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Can't execute getResultList", e);
            throw new IllegalStateException(e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    protected abstract String tableName();

    protected abstract List<T> parseResultSet(ResultSet resultSet);

}
