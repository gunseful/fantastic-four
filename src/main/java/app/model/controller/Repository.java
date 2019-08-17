package app.model.controller;

import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.encrypt.Encrypt;
import app.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Repository extends AbstractRepository {

    public static Logger logger = LogManager.getLogger();
    private DataSource dataSource = getDataSource();
    private ConnectionPool connectionPool = getConnectionPool();

    public synchronized List<Product> getList() {

        List<Product> list = new ArrayList<>();
        Connection connection = null;
        try {
            connectionPool.printDbStatus();
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM PRODUCTS");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                int price = rs.getInt("PRICE");
                list.add(new Product(id, name, price));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            try {
                connectionPool.printDbStatus();
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return list;
    }


    public synchronized void payOrder(int id) {
        //меняет статус заказа на Оплачено
        Connection connection = null;
        String sql = "UPDATE ORDERS\n" +
                "SET STATE = 'PAID'\n" +
                "WHERE ORDER_ID = " + id;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            logger.info("product " + id + " has been deleted from basket list");
        } catch (SQLException e) {
            logger.info("Fail connect to database");
            logger.error(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized boolean addToBasket(User user, int productID, boolean add) {
        //добавляет новый товар в кросс таблицу ORDERS_PRODUCTS
        Connection connection = null;
        String sql;
        int orderId = 0;
        int count;
        try {
            sql = "SELECT * FROM ORDERS WHERE CUSTOMERID =" + user.getId() + "AND STATE = 'NOT_ORDERED'";
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderId = resultSet.getInt("ORDER_ID");
            }
            sql = "SELECT * FROM PRODUCTS_ORDERS WHERE PRODUCT_ID = " + productID + "AND ORDER_ID = " + orderId;
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("COUNT");
                if (add) {
                    if (count < 10) {
                        count++;
                    }
                } else {
                    if (count > 1) {
                        count--;
                    }
                }
                sql = "UPDATE PRODUCTS_ORDERS SET COUNT = " + count + " WHERE PRODUCT_ID = " + productID + "AND ORDER_ID = " + orderId;
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
            } else {
                sql = "SELECT * FROM PRODUCTS_ORDERS WHERE ORDER_ID = " + orderId;
                preparedStatement = connection.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();
                int size = 0;
                if (resultSet != null) {
                    resultSet.last();
                    size = resultSet.getRow();
                }
                if (size >= 10) {
                    return false;
                }
                sql = "INSERT INTO PRODUCTS_ORDERS (PRODUCT_ID, ORDER_ID) Values (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, productID);
                preparedStatement.setInt(2, orderId);
                preparedStatement.executeUpdate();
            }
            logger.info("User=" + user.getNickname() + " added " + productID + " product to his basket");
            return true;
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized void deleteFromBasket(User user, int productID) {
        //добавляет новый товар в кросс таблицу ORDERS_PRODUCTS
        Connection connection = null;
        String sql = "";
        int orderId = 0;
        int count = 0;
        try {
            sql = "SELECT * FROM ORDERS WHERE CUSTOMERID =" + user.getId() + " AND STATE = 'NOT_ORDERED'";
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderId = resultSet.getInt("ORDER_ID");
                System.out.println(orderId);
            }
            System.out.println(productID);
            sql = "DELETE FROM PRODUCTS_ORDERS WHERE PRODUCT_ID = " + productID + " AND ORDER_ID = " + orderId;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            logger.info("User=" + user.getNickname() + " delete " + productID + " from his basket");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized List<Product> getBasket(User user) {
        //возвращает лист с продуктами пользователя который запросил
        Connection connection = null;
        String sql = "";
        int orderId = 0;
        List<Product> list = new ArrayList<>();
        try {
            sql = "SELECT * FROM ORDERS WHERE CUSTOMERID =" + user.getId() + "AND STATE = 'NOT_ORDERED'";
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderId = resultSet.getInt("ORDER_ID");
            }
            sql = "SELECT * FROM PRODUCTS_ORDERS\n" +
                    "INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID\n" +
                    "INNER JOIN ORDERS ON ORDERS.ORDER_ID = PRODUCTS_ORDERS.ORDER_ID\n" +
                    "where PRODUCTS_ORDERS.ORDER_ID = " + orderId;
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            getProducts(resultSet, list);
            return list;
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized void makeOrder(User user) {
        //меняет статус корзины на статус заказа и создает новую пустую корзину
        Connection connection = null;
        String sql = "";
        int orderId = 0;
        try {
            sql = "SELECT * FROM ORDERS WHERE CUSTOMERID =" + user.getId() + "AND STATE = 'NOT_ORDERED'";
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderId = resultSet.getInt("ORDER_ID");
            }
            LocalDate creationDate = LocalDate.now();
            sql = "UPDATE ORDERS SET STATE = 'ORDERED', CREATEDAT=? WHERE ORDER_ID = " + orderId;
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, creationDate);
            preparedStatement.executeUpdate();
            sql = "INSERT INTO ORDERS (CUSTOMERID, STATE) Values (?, 'NOT_ORDERED')";
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized List<Order> getOrders(User user) {
        //здесь получаем список заказов, если юзер простой клиент он получает только свои заказы, если админ - все заказы
        Connection connection = null;
        String sql = "";
        int orderId = 0;
        boolean isPaid = false;
        List<Order> list = new ArrayList<>();
        try {
            sql = (!user.isAdministrator()) ? "SELECT * FROM ORDERS WHERE CUSTOMERID =" + user.getId() + "AND STATE = 'ORDERED' OR STATE = 'PAID'" :
                    "SELECT * FROM ORDERS WHERE STATE = 'ORDERED' OR STATE = 'PAID'";
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderId = resultSet.getInt("ORDER_ID");
                java.sql.Date dbSqlDate = resultSet.getDate("CREATEDAT");
                int customerId = resultSet.getInt("CUSTOMERID");
                if (resultSet.getString("STATE").equals("PAID")) {
                    isPaid = true;
                }
                String sqlProducts = "SELECT * FROM PRODUCTS_ORDERS\n" +
                        "INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID\n" +
                        "INNER JOIN ORDERS ON ORDERS.ORDER_ID = PRODUCTS_ORDERS.ORDER_ID\n" +
                        "where PRODUCTS_ORDERS.ORDER_ID = " + orderId;
                PreparedStatement preparedStatementProducts = connection.prepareStatement(sqlProducts);
                ResultSet resultSetProducts = preparedStatementProducts.executeQuery();
                List<Product> products = new ArrayList<>();
                getProducts(resultSetProducts, products);
                list.add(new Order(orderId, dbSqlDate, products, isPaid, getUser(customerId)));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Fail connect to database");
            logger.error(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return list;
    }

    private void getProducts(ResultSet resultSetProducts, List<Product> products) throws SQLException {
        while (resultSetProducts.next()) {
            String name = resultSetProducts.getString("NAME");
            int price = resultSetProducts.getInt("PRICE");
            int id = resultSetProducts.getInt("ID");
            int count = resultSetProducts.getInt("COUNT");
            products.add(new Product(id, name, price, count));
        }
    }

    public synchronized void deleteOrder(int id) {
        Connection connection = null;
        String sql = "";
        try {
            sql = "DELETE FROM PRODUCTS_ORDERS  WHERE ORDER_ID = " + id;
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            sql = "DELETE FROM ORDERS WHERE ORDER_ID = " + id;
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            logger.info("product " + id + " has been deleted from order list");
        } catch (SQLException e) {
            logger.info("Fail connect to database");
            logger.error(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }


    public synchronized User getUser(int id) {
        Connection connection = null;
        //получаем юзера из базы данных по его айдишнику
        String sql = "SELECT * FROM Users WHERE Id = ?";

        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            //создаем объект юзер и добавляем в него данные из базыданных
            User user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("ID"));
                user.setNickname(resultSet.getString("NICKNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setName(resultSet.getString("NAME"));
                user.setAdministrator(resultSet.getBoolean("ISADMIN"));
                user.setInBlackList(resultSet.getBoolean("ISBLOCKED"));
            }
            return user;
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized boolean checkBlackList(User user) {
        //проверяет пользователя в базе данных, если isblocked = true значит он заблокированный
        Connection connection = null;
        String sql = "SELECT * FROM USERS WHERE ID = " + user.getId();
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            return resultSet.getBoolean("ISBLOCKED");

        } catch (Exception ex) {
            logger.error("Connection failed...");
            logger.error(ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return false;
    }

    public synchronized User getUserByNickName(String nickname) {
        Connection connection = null;
        //получаем юзера из базы данных по никнейму
        String sql = "SELECT * FROM Users WHERE nickname = ?";
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nickname.toUpperCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getInt("ID"));
                user.setNickname(resultSet.getString("NICKNAME"));
                user.setPassword(resultSet.getString("PASSWORD"));
                user.setName(resultSet.getString("NAME"));
                user.setAdministrator(resultSet.getBoolean("ISADMIN"));
            }
            return user;
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            logger.error(ex);
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized boolean addNewUser(User user) {
        Connection connection = null;
        String sql = "INSERT INTO Users (Nickname, Password, Name) Values (?, ?, ?)";
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getNickname());
            String password = Encrypt.encrypt(user.getPassword(), "secret key");
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, user.getName());
            preparedStatement.executeUpdate();
            logger.info("New User " + user.getNickname() + " has been added to database");

            sql = "INSERT INTO ORDERS (CUSTOMERID, STATE) Values (?,'NOT_ORDERED')";
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized boolean checkLogginAndPassword(User user) {
        String sql = "SELECT * FROM Users WHERE Nickname = ? AND PASSWORD = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //ищем в базе данных ник и пароль совпадения если есть хоть одно (а больше и не может быть) то кидаем тру, если не нашли то фелс
            String password = Encrypt.encrypt(user.getPassword(), "secret key");
            preparedStatement.setString(1, user.getNickname().toUpperCase());
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
                ;
            }
        }
    }


    public synchronized List<User> getBlackList() {
        //из списка пользователей получает только тех у кого столбец isblocked = true
        Connection connection = null;
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE ISBLOCKED = TRUE";
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user;
            while (resultSet.next()) {
                user = new User();
                int id = resultSet.getInt("ID");
                user.setId(id);
                String nickname = resultSet.getString("NICKNAME");
                user.setNickname(nickname);
                String password = resultSet.getString("PASSWORD");
                user.setPassword(password);
                String name = resultSet.getString("NAME");
                user.setName(name);
                boolean isAdmin = resultSet.getBoolean("ISADMIN");
                user.setAdministrator(isAdmin);
                list.add(user);
            }
        } catch (Exception ex) {
            logger.error("Connection failed...");
            logger.error(ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return list;
    }

    public synchronized void deleteFromBlackList(int id) {
        //удаляет из черного списка, то есть меняет значение isblocked на false
        String sql = "UPDATE Users SET ISBLOCKED = FALSE WHERE id = " + id;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            logger.info("user " + id + " has been deleted from black list");
        } catch (SQLException e) {
            logger.info("Fail connect to database");
            System.out.println("Connection failed...");
            logger.error(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized void addUserToBlackList(User user) {
        //добавляет в черный список, isblocked = true
        String sql = "UPDATE Users SET ISBLOCKED = TRUE WHERE id = " + user.getId();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            logger.info("User= " + user.getNickname() + " was added to blacklist");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }


    public synchronized void deleteProduct(String id) {
        Connection connection = null;
        try {
            connectionPool.printDbStatus();
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM PRODUCTS WHERE Id = " + id);
            ps.executeUpdate();
            logger.info("product " + id + " has been deleted from product list");
        } catch (SQLException e) {
            logger.error("Fail connect to database");
            logger.error(e);
        } finally {
            connectionPool.printDbStatus();
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    public synchronized void add(Product product) {
        String sql = "INSERT INTO PRODUCTS (Name, Price) Values (?, ?)";
        Connection connection = null;
        try {
            connectionPool.printDbStatus();
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.executeUpdate();
            logger.info("product " + product.getName() + " has been added to product list");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            logger.error(ex);
        } finally {
            try {
                connectionPool.printDbStatus();
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }


}
