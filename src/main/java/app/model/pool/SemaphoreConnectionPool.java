package app.model.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.Semaphore;

public class SemaphoreConnectionPool {

    public static Logger logger = LogManager.getLogger();
    private static Semaphore semaphore = new Semaphore(2);
    private static SemaphoreConnectionPool semaphoreConnectionPool = new SemaphoreConnectionPool();

    private SemaphoreConnectionPool(){
        System.out.println("inside constructor");
    }

    public static SemaphoreConnectionPool getInstance()
    {
        return semaphoreConnectionPool;
    }

    public Connection getConnection() throws InterruptedException, SQLException {
        System.out.println("semaphore before getting connection "+        semaphore.availablePermits());
        Properties connectionProps = new Properties();
        connectionProps.put("user", "");
        connectionProps.put("password", "");
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        semaphore.acquire();
        Connection con = makeConnection(connectionProps);
        System.out.println("semaphore after getting connection "+semaphore.availablePermits());
        return (con);
    }


    public  void releaseConnection(Connection connection) throws SQLException {
        System.out.println("semaphore before release connection "+semaphore.availablePermits());
        connection.close();
        semaphore.release();
        System.out.println("semaphore after release connection "+semaphore.availablePermits());
    }

    private  Connection makeConnection(Properties connectionProps) throws SQLException {
        Connection connection;
        connection = DriverManager.getConnection("jdbc:h2:/c:/Users/Ares/IdeaProjects/fantasticFour/db/fantasticFour", connectionProps);
        System.out.println("Connected to database");
        return connection;
    }
}
