package app.model.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    public static Logger logger = LogManager.getLogger();
    private static final int MAX_SIZE = 2;
    private BlockingQueue<Connection> queue;
    private static ConnectionPool connectionPool = new ConnectionPool();

    private ConnectionPool(){
        System.out.println("inside constructor");
        queue = new ArrayBlockingQueue<>(MAX_SIZE);
        Properties connectionProps = new Properties();
        connectionProps.put("user", "");
        connectionProps.put("password", "");
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        for (int i = 0; i < MAX_SIZE; i++) {
            try {
                queue.put(makeConnection(connectionProps));
            } catch (InterruptedException | SQLException e) {
                logger.error(e);
            }
        }

        System.out.println("total size:" + queue.size());
    }
    public static ConnectionPool getInstance()
    {
        return connectionPool;
    }

    public  Connection getConnection() throws InterruptedException {
        System.out.println("size before getting connection" + queue.size());
        Connection con = queue.take();
        System.out.println("size after getting connection" + queue.size());
        return (con);
    }

    public  void releaseConnection(Connection con) throws InterruptedException {
        System.out.println("size before releasing connection" + queue.size());
        queue.put(con);
        System.out.println("size after releasing connection" + queue.size());
    }

    private  Connection makeConnection(Properties connectionProps) throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection("jdbc:h2:/c:/Users/Ares/IdeaProjects/fantasticFour/db/fantasticFour", connectionProps);
        System.out.println("Connected to database");
        return connection;
    }
}
