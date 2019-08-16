package app.model.controller;

import app.model.pool.ConnectionPool;

import javax.sql.DataSource;

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
}
