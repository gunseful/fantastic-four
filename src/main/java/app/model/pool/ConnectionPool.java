package app.model.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
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
    private static ConnectionPool connectionPool = new ConnectionPool();

    private ConnectionPool() {
        logger.info("inside ConnectionPool constructor");
        queue = new ArrayBlockingQueue<>(MAX_SIZE);
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("C:\\Users\\Ares\\IdeaProjects\\fantasticFour\\db\\connection.properties"));
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
        logger.info("total size of connection: " + queue.size());
    }

    public static ConnectionPool getInstance() {
        return connectionPool;
    }

    public Connection getConnection() throws InterruptedException {
        logger.info("size before getting connection" + queue.size());
        Connection connection = queue.take();
        logger.info("size after getting connection" + queue.size());
        return (connection);
    }

    public void releaseConnection(Connection con) throws InterruptedException {
        logger.info("size before releasing connection" + queue.size());
        queue.put(con);
        logger.info("size after releasing connection" + queue.size());
    }

    private Connection makeConnection(Properties properties) throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection(properties.getProperty("URL"), properties);
        logger.info("Connected to database");
        return connection;
    }
}
