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
    private static Connection connection = null;

    void saveOrUpdate(String sql, StatementTransformer<PreparedStatement> transformer) {
        try {
            connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            transformer.transform(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Fail connect to database", e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }


    @Override
    public Optional<T> findById(int id) {
        return getSingleResult((String.format("SELECT * FROM %s WHERE ID = ?", tableName())), preparedStatement ->
                preparedStatement.setInt(1, id));
    }

    Optional<T> getSingleResult(String sql, StatementTransformer<PreparedStatement> transformer) {
        final List<T> resultList = getResultList(sql, transformer);
        if (resultList.size() > 1) {
            throw new NonUniqueResultException("Non single result. Found - " + resultList.size());
        }
        return resultList.stream().findFirst();
    }


    List<T> getResultList(String sql, StatementTransformer<PreparedStatement> transformer) {
        final Connection connection = connectionPool.getConnection();
        try {
            final PreparedStatement statement = connection.prepareStatement(sql);
            transformer.transform(statement);
            final ResultSet resultSet = statement.executeQuery();
            //Structural pattern - Template method
            return parseResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Can't execute getResultList", e);
            throw new IllegalStateException(e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    List<T> getResultList(String sql) {
        return getResultList(sql, t -> {
        });
    }

    public List<T> getAll() {
        return getResultList(String.format("Select * from %s", tableName()));
    }

    protected abstract String tableName();

    protected abstract List<T> parseResultSet(ResultSet resultSet);

}
