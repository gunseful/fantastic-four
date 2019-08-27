//package app.model.repository;
//
//import app.entities.products.Order;
//import app.entities.products.Product;
//import app.entities.user.User;
//import app.model.encrypt.Encrypt;
//import app.model.pool.ConnectionPool;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Repository {
//
//    public static Logger logger = LogManager.getLogger();
//    private ConnectionPool connectionPool = ConnectionPool.getInstance();
//
//
//    private synchronized void updateSql(String sql) {
//        Connection connection = null;
//        try {
//            connection = connectionPool.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.executeUpdate();
//        } catch (SQLException | InterruptedException e) {
//            logger.info("Fail connect to database");
//            logger.error(e);
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//    public synchronized void add(Product product) {
//        updateSql("INSERT INTO PRODUCTS (Name, Price) Values ('" + product.getName() + "', '" + product.getPrice() + "')");
//        logger.info("product " + product.getName() + " has been added to product list");
//    }
//
//    public synchronized void deleteProduct(String id) {
//        updateSql("DELETE FROM PRODUCTS WHERE Id = " + id);
//        logger.info("product " + id + " has been deleted from product list");
//    }
//
//    public synchronized void addUserToBlackList(User user) {
//        //добавляет в черный список, is_blocked = true
//        updateSql("UPDATE Users SET IS_BLOCKED = TRUE WHERE id = " + user.getId());
//        logger.info("User= " + user.getNickname() + " was added to blacklist");
//    }
//
//    public synchronized void deleteFromBlackList(int id) {
//        //удаляет из черного списка, то есть меняет значение isblocked на false
//        updateSql("UPDATE Users SET IS_BLOCKED = FALSE WHERE id = " + id);
//        logger.info("user " + id + " has been deleted from black list");
//    }
//
//    public synchronized void payOrder(int id) {
//        //меняет статус заказа на Оплачено
//        updateSql("UPDATE ORDERS SET STATE = 'PAID' WHERE ID = " + id);
//        logger.info("product " + id + " has been deleted from basket list");
//    }
//
//    public synchronized void deleteFromBasket(User user, int productId) {
//        //добавляет новый товар в кросс таблицу ORDERS_PRODUCTS
//        updateSql("DELETE FROM PRODUCTS_ORDERS WHERE PRODUCTS_ORDERS.ORDER_ID IN (SELECT ORDERS.ID FROM ORDERS WHERE CUSTOMER_ID = " + user.getId() + " AND STATE='NOT_ORDERED') AND PRODUCT_ID = " + productId + "");
//        logger.info("User=" + user.getNickname() + " delete " + productId + " from his basket");
//    }
//
//    private synchronized String addOrDelete(boolean add, int userId, int produtctId) {
//        return (add) ? "update PRODUCTS_ORDERS PR set PR.COUNT = PR.COUNT+1 where exists (select * from ORDERS O where PR.ORDER_ID  = O.ID AND O.CUSTOMER_ID = " + userId + " AND O.STATE='NOT_ORDERED' AND PR.PRODUCT_ID=" + produtctId + ")"
//                : "update PRODUCTS_ORDERS PR set PR.COUNT = PR.COUNT-1 where exists (select * from ORDERS O where PR.ORDER_ID  = O.ID AND O.CUSTOMER_ID = " + userId + " AND O.STATE='NOT_ORDERED' AND PR.PRODUCT_ID=" + produtctId + ")";
//    }
//
//    public synchronized boolean updateBasket(User user, int productId, boolean add) {
//        //если товар уже есть в корзине увеличивает или уменьшает количество на 1
//        updateSql((addOrDelete(add, user.getId(), productId)));
//        if (!checkCount(user, productId)) {
//            deleteFromBasket(user, productId);
//            return false;
//        }
//        return true;
//    }
//
//    public synchronized boolean addNewUser(User user) {
//        String password = Encrypt.encrypt(user.getPassword(), "secret key");
//        updateSql("INSERT INTO Users (Nickname, Password, Name) Values ('" + user.getNickname() + "', '" + password + "', '" + user.getName() + "')");
//        logger.info("New User " + user.getNickname() + " has been added to database");
//        return true;
//    }
//
//
//
//
//
//
//
//    public synchronized List<Product> getList() {
//
//        List<Product> list = new ArrayList<>();
//        Connection connection = null;
//        try {
//            connection = connectionPool.getConnection();
//            PreparedStatement ps = connection.prepareStatement("SELECT * FROM PRODUCTS");
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("ID");
//                String name = rs.getString("NAME");
//                int price = rs.getInt("PRICE");
//                list.add(new Product(id, name, price));
//            }
//        } catch (SQLException | InterruptedException e) {
//            logger.error(e);
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//        return list;
//    }
//
//
//
//    public synchronized boolean addToBasket(User user, int productID) {
//        //добавляет товар в корзину
//        Connection connection = null;
//        String sql;
//        try {
//            connection = connectionPool.getConnection();
//            sql = "SELECT * FROM ORDERS WHERE CUSTOMER_ID = " + user.getId() + " AND STATE = 'NOT_ORDERED'";
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (!resultSet.next()) {
//                sql = "INSERT INTO ORDERS (CUSTOMER_ID, STATE) Values (?, 'NOT_ORDERED')";
//                preparedStatement = connection.prepareStatement(sql);
//                preparedStatement.setInt(1, user.getId());
//                preparedStatement.executeUpdate();
//            }
//            sql = "INSERT INTO PRODUCTS_ORDERS (PRODUCT_ID , ORDER_ID ) VALUES(" + productID + ", (SELECT ID FROM ORDERS WHERE CUSTOMER_ID = " + user.getId() + " AND STATE = 'NOT_ORDERED'))";
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.executeUpdate();
//            return true;
//        } catch (Exception e) {
//            logger.error(e);
//            return false;
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//    private synchronized boolean checkCount(User user, int productId) {
//        String sql;
//        Connection connection = null;
//        try {
//            connection = connectionPool.getConnection();
//            sql = "SELECT * FROM PRODUCTS_ORDERS inner join orders on PRODUCTS_ORDERS.ORDER_ID = ORDERS.ID where PRODUCTS_ORDERS.PRODUCT_ID = ? AND ORDERS.CUSTOMER_ID = ? AND ORDERS.STATE = 'NOT_ORDERED'";
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, productId);
//            preparedStatement.setInt(2, user.getId());
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (!resultSet.next()) {
//                return false;
//            }
//            int count = resultSet.getInt("COUNT");
//            return count != 0;
//        } catch (Exception e) {
//            logger.error(e);
//            return false;
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//
//
//
//
//    public synchronized List<Product> getBasket(User user) {
//        //возвращает лист с продуктами пользователя который запросил
//        Connection connection = null;
//        String sql;
//        List<Product> list = new ArrayList<>();
//        try {
//            connection = connectionPool.getConnection();
//            sql = "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.STATE  = 'NOT_ORDERED' AND  ORDERS .CUSTOMER_ID =" + user.getId();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                String name = resultSet.getString("NAME");
//                int price = resultSet.getInt("PRICE");
//                int id = resultSet.getInt("ID");
//                int count = resultSet.getInt("COUNT");
//                list.add(new Product(id, name, price, count));
//            }
//            return list;
//        } catch (Exception ex) {
//            logger.info("Fail connect to database");
//            logger.error(ex);
//            return null;
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//    public synchronized void makeOrder(User user) {
//        //меняет статус корзины на статус заказа и создает новую пустую корзину
//        Connection connection = null;
//        String sql;
//        try {
//            connection = connectionPool.getConnection();
//            LocalDate creationDate = LocalDate.now();
//            sql = "UPDATE ORDERS SET STATE = 'ORDERED', CREATEDAT=? WHERE CUSTOMER_ID = " + user.getId() + " AND STATE = 'NOT_ORDERED'";
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setObject(1, creationDate);
//            preparedStatement.executeUpdate();
//        } catch (Exception ex) {
//            logger.info("Fail connect to database");
//            logger.error(ex);
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//    public synchronized List<Order> getOrders(User user) {
//        //здесь получаем список заказов, если юзер простой клиент он получает только свои заказы, если админ - все заказы
//        Connection connection = null;
//        String sql;
//        int orderId = 0;
//        boolean isPaid;
//        List<Order> list = new ArrayList<>();
//        try {
//            sql = (!user.isAdministrator()) ? "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.CUSTOMER_ID=2 AND ORDERS.STATE = 'ORDERED' OR STATE ='PAID' " :
//                    "SELECT * FROM PRODUCTS_ORDERS INNER JOIN PRODUCTS ON PRODUCTS.ID = PRODUCTS_ORDERS.PRODUCT_ID INNER JOIN ORDERS ON ORDERS.ID = PRODUCTS_ORDERS.ORDER_ID where ORDERS.STATE = 'ORDERED' OR STATE ='PAID'";
//            connection = connectionPool.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                if (!containsOrder(list, resultSet.getInt("ORDER_ID"))) {
//                    orderId = resultSet.getInt("ORDER_ID");
//                    java.sql.Date dbSqlDate = resultSet.getDate("CREATEDAT");
//                    int customerId = resultSet.getInt("CUSTOMER_ID");
//                    isPaid = resultSet.getString("STATE").equals("PAID");
//                    List<Product> products = new ArrayList<>();
//                    list.add(new Order(orderId, dbSqlDate, products, isPaid, getUser(customerId)));
//                }
//                for (Order order : list) {
//                    if (resultSet.getInt("ORDER_ID") == orderId) {
//                        order.addProduct(new Product(resultSet.getInt("PRODUCT_ID"), resultSet.getString("NAME"), resultSet.getInt("PRICE"), resultSet.getInt("COUNT")));
//                    }
//                }
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("Fail connect to database");
//            logger.error(e);
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//        return list;
//    }
//
//    public synchronized void deleteOrder(int id) {
//        Connection connection = null;
//        try {
//            connection = connectionPool.getConnection();
//            connection.setAutoCommit(false);
//            String sqlProductOrders = "DELETE FROM PRODUCTS_ORDERS  WHERE ORDER_ID = " + id;
//            PreparedStatement preparedStatementPr = connection.prepareStatement(sqlProductOrders);
//            preparedStatementPr.executeUpdate();
//            String sqlOrders = "DELETE FROM ORDERS WHERE ID = " + id;
//            PreparedStatement preparedStatementO = connection.prepareStatement(sqlOrders);
//            preparedStatementO.executeUpdate();
//            connection.commit();
//            connection.setAutoCommit(true);
//            logger.info("product " + id + " has been deleted from order list");
//        } catch (SQLException | InterruptedException e) {
//            logger.info("Fail connect to database");
//            logger.error(e);
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//
//    public synchronized User getUser(int id) {
//        Connection connection = null;
//        //получаем юзера из базы данных по его айдишнику
//        String sql = "SELECT * FROM Users WHERE ID = ?";
//        try {
//            connection = connectionPool.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setInt(1, id);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            //создаем объект юзер и добавляем в него данные из базыданных
//            User user = new User();
//            while (resultSet.next()) {
//                user.setId(resultSet.getInt("ID"));
//                user.setNickname(resultSet.getString("NICKNAME"));
//                user.setPassword(resultSet.getString("PASSWORD"));
//                user.setName(resultSet.getString("NAME"));
//                user.setAdministrator(resultSet.getBoolean("IS_ADMIN"));
//                user.setInBlackList(resultSet.getBoolean("IS_BLOCKED"));
//            }
//            return user;
//        } catch (Exception ex) {
//            logger.info("Fail connect to database");
//            logger.error(ex);
//            return null;
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//    public synchronized boolean checkBlackList(User user) {
//        //проверяет пользователя в базе данных, если isblocked = true значит он заблокированный
//        Connection connection = null;
//        String sql = "SELECT * FROM USERS WHERE ID = " + user.getId();
//        try {
//            connection = connectionPool.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            resultSet.next();
//            return resultSet.getBoolean("IS_BLOCKED");
//        } catch (Exception ex) {
//            logger.error("Connection failed...");
//            logger.error(ex);
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//        return false;
//    }
//
//    public synchronized User getUserByNickName(String nickname) {
//        Connection connection = null;
//        //получаем юзера из базы данных по никнейму
//        String sql = "SELECT * FROM Users WHERE nickname = ?";
//        try {
//            connection = connectionPool.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, nickname.toUpperCase());
//            ResultSet resultSet = preparedStatement.executeQuery();
//            User user = new User();
//            while (resultSet.next()) {
//                user.setId(resultSet.getInt("ID"));
//                user.setNickname(resultSet.getString("NICKNAME"));
//                user.setPassword(resultSet.getString("PASSWORD"));
//                user.setName(resultSet.getString("NAME"));
//                user.setAdministrator(resultSet.getBoolean("IS_ADMIN"));
//            }
//            return user;
//        } catch (Exception ex) {
//            System.out.println("Connection failed...");
//            logger.error(ex);
//            return null;
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//
//
//    public synchronized boolean checkLogginAndPassword(User user) {
//        String sql = "SELECT * FROM Users WHERE Nickname = ? AND PASSWORD = ?";
//        Connection connection = null;
//        try {
//            connection = connectionPool.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            //ищем в базе данных ник и пароль совпадения если есть хоть одно (а больше и не может быть) то кидаем тру, если не нашли то фелс
//            String password = Encrypt.encrypt(user.getPassword(), "secret key");
//            preparedStatement.setString(1, user.getNickname().toUpperCase());
//            preparedStatement.setString(2, password);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            return resultSet.next();
//        } catch (Exception ex) {
//            logger.info("Fail connect to database");
//            logger.error(ex);
//            return false;
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//    }
//
//
//    public synchronized List<User> getBlackList() {
//        //из списка пользователей получает только тех у кого столбец isblocked = true
//        Connection connection = null;
//        List<User> list = new ArrayList<>();
//        String sql = "SELECT * FROM USERS WHERE IS_BLOCKED = TRUE";
//        try {
//            connection = connectionPool.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            User user;
//            while (resultSet.next()) {
//                user = new User();
//                int id = resultSet.getInt("ID");
//                user.setId(id);
//                String nickname = resultSet.getString("NICKNAME");
//                user.setNickname(nickname);
//                String password = resultSet.getString("PASSWORD");
//                user.setPassword(password);
//                String name = resultSet.getString("NAME");
//                user.setName(name);
//                boolean isAdmin = resultSet.getBoolean("IS_ADMIN");
//                user.setAdministrator(isAdmin);
//                list.add(user);
//            }
//        } catch (Exception ex) {
//            logger.error("Connection failed...");
//            logger.error(ex);
//        } finally {
//            try {
//                connectionPool.releaseConnection(connection);
//            } catch (InterruptedException e) {
//                logger.error(e);
//            }
//        }
//        return list;
//    }
//
//
//
//    private synchronized boolean containsOrder(final List<Order> list, final int id) {
//        return list.stream().anyMatch(o -> o.getId() == id);
//    }
//}