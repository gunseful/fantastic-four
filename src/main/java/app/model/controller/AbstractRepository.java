package app.model.controller;

import app.model.pool.ConnectionPool;

import javax.sql.DataSource;

public abstract class AbstractRepository {
    private ConnectionPool connectionPool;
    private DataSource dataSource;

    public AbstractRepository() {
        connectionPool = new ConnectionPool();
        dataSource = ConnectionPool.getDataSource();
    }

    ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    DataSource getDataSource() {
        return dataSource;
    }
}
