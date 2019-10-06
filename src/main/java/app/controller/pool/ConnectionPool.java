package app.controller.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ConnectionPool {

    public static Logger logger = LogManager.getLogger();
    private static final int MAX_SIZE = 5;
    private BlockingQueue<Connection> queue;
    private final static ConnectionPool connectionPool = new ConnectionPool();
    private Properties properties = new Properties();


    private ConnectionPool() {
        logger.debug("inside ConnectionPool constructor");
        queue = new ArrayBlockingQueue<>(MAX_SIZE);
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("connection.properties");
            if (inputStream!=null){
                properties.load(inputStream);
            }else{
                throw new FileNotFoundException("property file not found");
            }
        } catch (IOException e) {
            logger.error(e);
        }
        try {
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        for (int i = 0; i < MAX_SIZE; i++) {
            try {
                queue.put(makeConnection(properties));
            } catch (InterruptedException | SQLException e) {
                logger.error(e);
            }
        }
        logger.debug("total size of connection: {}", queue.size());
    }

    public static ConnectionPool getInstance() {
        return connectionPool;
    }

    public Connection getConnection() {
        try {
            return queue.poll(5, SECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public void releaseConnection(Connection con) {
        try {
            if(con.isValid(0)) {
                queue.put(con);
            }else{
                queue.put(makeConnection(properties));
            }
        } catch (InterruptedException | SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Connection makeConnection(Properties properties) throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection(properties.getProperty("URL"), properties);
        logger.info("Connected to database");
        return connection;
    }
}
