package app.model.pool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private Connection connection;

    private ConnectionPool(){
    }

    private static ConnectionPool instance = null;

    public static ConnectionPool getInstance(){
        if (instance==null)
            instance = new ConnectionPool();
        return instance;
    }

    public Connection returnConnection(Connection connection){
        return connection;
    }

    public Connection getConnection(){
        Context context;
        try {
            context = new InitialContext();
            DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/fantasticFour");
            connection = ds.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}