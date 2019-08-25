package app.model.pool;

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

public class ConnectionPool {

    public static Logger logger = LogManager.getLogger();
    private static final int MAX_SIZE = 5;
    private BlockingQueue<Connection> queue;
    private final static ConnectionPool connectionPool = new ConnectionPool();

    private ConnectionPool() {
        logger.debug("inside ConnectionPool constructor");
        queue = new ArrayBlockingQueue<>(MAX_SIZE);
        Properties properties = new Properties();
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
        logger.debug("total size of connection: " + queue.size());
    }

    public static ConnectionPool getInstance() {
        return connectionPool;
    }

    public Connection getConnection() throws InterruptedException {
        logger.debug("size before getting connection" + queue.size());
        Connection connection = queue.take();
        logger.debug("size after getting connection" + queue.size());
        return (connection);
    }

    public void releaseConnection(Connection con) throws InterruptedException {
        logger.debug("size before releasing connection" + queue.size());
        queue.put(con);
        logger.debug("size after releasing connection" + queue.size());
    }

    private Connection makeConnection(Properties properties) throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection(properties.getProperty("URL"), properties);
        logger.debug("Connected to database");
        return connection;
    }
}
