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
    public static Connection connection = null;

    public PreparedStatement getPreparedStatement(String sql) {
        PreparedStatement ps = null;
        try {
            connection = connectionPool.getConnection();
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            logger.error("Fail connect to database",e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return ps;
    }

    //todo make it Optional<T>
    @Override
    public T findById(int id) {
        return getSingleResult((String.format("SELECT * FROM %s WHERE ID = %d", tableName(), id))).orElseThrow(IllegalStateException::new);
    }

    //todo изначальная версия моего findBy, в одном случае вместо AND нужно кидать OR поэтому принял решение просто строкой параметров искать
    //todo может как-то логику можно сделать чтобы коллекцию и он сам хуяк хуяк, пока хз
//    public List<T> findBy(LinkedList<String> parameters) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("SELECT * FROM %s WHERE ", tableName()));
//        parameters.stream().filter(o -> !o.equals(parameters.getLast())).forEach(o -> sb.append(o).append(" AND "));
//        parameters.stream().filter(o -> o.equals(parameters.getLast())).forEach(sb::append);
//        return getResultList(sb.toString());
//    }

    /**
     * fixme if you would like to pass parameters, the best way to avoid 'String typization'
     */
    @Override
    public List<T> findBy(String parameters) {
        String sb = String.format("SELECT * FROM %s WHERE ", tableName()) +
            parameters;
        return getResultList(sb);
    }

    @Override
    public Optional<T> singleFindBy(String parameters) {
        return findBy(parameters).stream().findFirst();
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

    protected List<T> getResultList(String sql, StatementTransformer<PreparedStatement> transformer) {
        final Connection connection = connectionPool.getConnection();
        try {
            final PreparedStatement statement = connection.prepareStatement(sql);
            transformer.transform(statement);
            final ResultSet resultSet = statement.executeQuery();
            //Structural pattern - `Template method`
            return parseResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Can't execute getResultList", e);
            throw new IllegalStateException(e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    /**
     * You should only return connection when you're finished all the job with it
     */
    protected List<T> getResultList(String sql) {
        return getResultList(sql, t -> {
            //empty body
        });
    }

    protected abstract String tableName();

    protected abstract List<T> parseResultSet(ResultSet resultSet);

}
