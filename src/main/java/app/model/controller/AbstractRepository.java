package app.model.controller;

import app.model.pool.ConnectionPool;

import javax.sql.DataSource;

public abstract class AbstractRepository {
    private ConnectionPool connectionPool = new ConnectionPool();
    private DataSource dataSource = ConnectionPool.getDataSource();

    public AbstractRepository() {
        connectionPool = new ConnectionPool();
        dataSource = ConnectionPool.getDataSource();
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
