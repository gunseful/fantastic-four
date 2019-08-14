package app.model.controller;

import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.encrypt.Encrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserController extends AbstractController {
    public static Logger logger = LogManager.getLogger();

    @Override
    public synchronized List<Product> getList() {
        List<Product> list = new ArrayList<>();
        PreparedStatement ps = getPrepareStatement("SELECT * FROM PRODUCTS");
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int price = rs.getInt(3);
                list.add(new Product(id, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }
        return list;
    }


    @Override
    public synchronized void deleteProduct(String id) {
        PreparedStatement ps = getPrepareStatement("DELETE FROM PRODUCTS WHERE Id = " + id);
        try {
            ps.executeUpdate();
            logger.info("product " + id + " has been deleted from product list");
        } catch (SQLException e) {
            logger.info("Fail connect to database");
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }
    }

    @Override
    public synchronized void add(Product product) {
        String sql = "INSERT INTO PRODUCTS (Name, Price) Values (?, ?)";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getPrice());
            preparedStatement.executeUpdate();
            logger.info("product " + product.getName() + " has been added to product list");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized User getUser(int id) {
        String sql = "SELECT * FROM Users WHERE Id = ?";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                user.setNickname(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setName(resultSet.getString(4));
                user.setAdministrator(resultSet.getBoolean(5));
                user.setInBlackList(resultSet.getBoolean("ISBLOCKED"));
            }
            return user;
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
            return null;
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized User getUserByNickName(String nickname) {
        String sql = "SELECT * FROM Users WHERE nickname = ?";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            preparedStatement.setString(1, nickname.toUpperCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = new User();
            while (resultSet.next()) {
                user.setId(resultSet.getInt(1));
                user.setNickname(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setName(resultSet.getString(4));
                user.setAdministrator(resultSet.getBoolean(5));
            }
            return user;
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            ex.printStackTrace();
            return null;
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized boolean addNewUser(User user) {
        String sql = "INSERT INTO Users (Nickname, Password, Name) Values (?, ?, ?)";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            preparedStatement.setString(1, user.getNickname());
            String password = Encrypt.encrypt(user.getPassword(), "secret key");
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, user.getName());
            preparedStatement.executeUpdate();
            logger.info("New User " + user.getNickname() + " has been added to database");
            return true;
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
            return false;
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized boolean checkLogginAndPassword(User user) {
        String sql = "SELECT * FROM Users WHERE Nickname = ? AND PASSWORD = ?";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            //gets all user list with User's nickname that you gave as attribute
            String password = Encrypt.encrypt(user.getPassword(), "secret key");
            preparedStatement.setString(1, user.getNickname().toUpperCase());
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;

            } else {
                return false;
            }
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
            return false;
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized void makeOrder(User user) {
        int orderId = 0;
        String sql1 = "SELECT * FROM BASKETS where customerid = ? AND isordered = false";
        PreparedStatement preparedStatement1 = getPrepareStatement(sql1);
        try {
            preparedStatement1.setInt(1, user.getId());
            ResultSet rs = preparedStatement1.executeQuery();
            rs.next();
            orderId = rs.getInt(6);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement1);
        }
        String sql2 = "UPDATE BASKETS \n" +
                "SET CREATEDAT=?," +
                "ID=?,\n" +
                "ISORDERED = TRUE\n" +
                "WHERE \n" +
                "\n" +
                "CUSTOMERID = ?  AND ISORDERED = FALSE";
        PreparedStatement preparedStatement2 = getPrepareStatement(sql2);
        try {
            //Insert String of order, User ID and Date when order was created to Table of Orders in db
            LocalDate creationDate = LocalDate.now();
            preparedStatement2.setObject(1, creationDate);
            preparedStatement2.setInt(2, orderId);
            preparedStatement2.setInt(3, user.getId());
            preparedStatement2.executeUpdate();
            logger.info("basket was ordered");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement2);
        }
    }

    @Override
    public synchronized void payOrder(int orderID) {
        String sql = "UPDATE BASKETS \n" +
                "SET ISPAID= TRUE\n" +
                "WHERE \n" +
                "\n" +
                "ID = ?  AND ISORDERED = TRUE";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            preparedStatement.setInt(1, orderID);
            preparedStatement.executeUpdate();
            logger.info("order was payed");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized List<Order> getOrders(User user) {
        String sql="";
        if(!user.isAdministrator()) {
            sql = "SELECT * FROM BASKETS INNER JOIN PRODUCTS ON PRODUCTS.ID = baskets.productid INNER JOIN USERS ON USERS.ID = baskets.customerid where USERS.id = ? AND ISORDERED = TRUE";
        }else{
            sql = "SELECT * FROM BASKETS INNER JOIN PRODUCTS ON PRODUCTS.ID = baskets.productid INNER JOIN USERS ON USERS.ID = baskets.customerid where ISORDERED = TRUE";
        }
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        List<Order> list = new ArrayList<>();
        try {
            if(!user.isAdministrator()) {
                preparedStatement.setInt(1, user.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            int currentOrderId = 0;
            Order order = null;
            while (resultSet.next()) {
                int orderId = resultSet.getInt(6);
                if (orderId != currentOrderId) {
                    if(currentOrderId!=0) {
                        list.add(order);
                    }
                    order = new Order();
                    currentOrderId = orderId;
                    LocalDate localDate = resultSet.getObject("CREATEDAT", LocalDate.class);
                    order.setCreationDate(localDate);
                    order.setProducts(new ArrayList<>());
                    order.setUser(getUser(resultSet.getInt("CUSTOMERID")));
                    boolean ispaid = resultSet.getBoolean(3);
                    order.setPaid(ispaid);
                    order.setId(currentOrderId);
                    order.setCustomerID(resultSet.getInt("CUSTOMERID"));
                }
                    int productId = resultSet.getInt("PRODUCTID");
                    String productName = resultSet.getString(8);
                    int productPrice = resultSet.getInt(9);
                    Product product = new Product(productId, productName, productPrice);
                    order.getProducts().add(product);
                }
            if(currentOrderId==0){
                return list;
            }
            if(list.isEmpty()){
                list.add(order);
            }else {
                if (order.getId() != list.get(list.size() - 1).getId()) {
                    list.add(order);
                }
            }
            } catch(Exception e){
                logger.info("Fail connect to database");
                e.printStackTrace();
            } finally{
                closePrepareStatement(preparedStatement);
            }
            return list;
        }

        @Override
        public synchronized void deleteOrder ( int id){
            PreparedStatement ps = getPrepareStatement("DELETE FROM BASKETS WHERE Id = " + id+" AND ISORDERED = TRUE");
            try {
                ps.executeUpdate();
                logger.info("Order= " + id + " was deleted from database");
            } catch (SQLException e) {
                logger.info("Fail connect to database");
                e.printStackTrace();
            } finally {
                closePrepareStatement(ps);
            }
        }


        @Override
        public synchronized List<User> getBlackList () {
            List<User> list = new ArrayList<>();
            PreparedStatement ps = getPrepareStatement("SELECT * FROM USERS WHERE ISBLOCKED = TRUE");
            try {
                ResultSet resultSet = ps.executeQuery();
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
                System.out.println("Connection failed...");
                System.out.println(ex);
            } finally {
                closePrepareStatement(ps);
            }
            return list;
        }


        @Override
        public synchronized void deleteFromBlackList ( int id){
            PreparedStatement ps = getPrepareStatement("UPDATE Users SET ISBLOCKED = FALSE WHERE id = " + id);
            try {
                ps.executeUpdate();
                logger.info("user " + id + " has been deleted from black list");
            } catch (SQLException e) {
                logger.info("Fail connect to database");
                System.out.println("Connection failed...");
                e.printStackTrace();
            } finally {
                closePrepareStatement(ps);
            }
        }


        @Override
        public synchronized void addUserToBlackList (User user){
            String sql = "UPDATE Users SET ISBLOCKED = TRUE WHERE id = " + user.getId();
            PreparedStatement preparedStatement = getPrepareStatement(sql);
            try {
                preparedStatement.executeUpdate();
                logger.info("User= " + user.getNickname() + " was added to blacklist");
            } catch (Exception ex) {
                logger.info("Fail connect to database");
                ex.printStackTrace();
            } finally {
                closePrepareStatement(preparedStatement);
            }
        }


        @Override
        public synchronized boolean checkBlackList (User user){
            PreparedStatement ps = getPrepareStatement("SELECT * FROM USERS WHERE ID = " + user.getId());
            try {
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getBoolean("ISBLOCKED") == true) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception ex) {
                System.out.println("Connection failed...");
                ex.printStackTrace();
            } finally {
                closePrepareStatement(ps);
            }
            return false;
        }


        @Override
        public synchronized void addToBasket (User user,int productID){
            String sql = "INSERT INTO BASKETS (CUSTOMERID, PRODUCTID) Values (?, ?)";
            PreparedStatement preparedStatement = getPrepareStatement(sql);
            try {
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setInt(2, productID);
                preparedStatement.executeUpdate();
                logger.info("User=" + user.getNickname() + " added " + productID + " product to his basket");
            } catch (Exception ex) {
                logger.info("Fail connect to database");
                ex.printStackTrace();
            } finally {
                closePrepareStatement(preparedStatement);
            }
        }


        public synchronized List<Product> getBasketList (User user){
            List<Product> list = new ArrayList<>();
            PreparedStatement ps = getPrepareStatement("    SELECT * FROM BASKETS\n" +
                    "    INNER JOIN PRODUCTS ON PRODUCTS.ID = baskets.productid\n" +
                    "    INNER JOIN USERS ON USERS.ID = baskets.customerid\n" +
                    "\n" +
                    "    where USERS.id = ? AND ISORDERED = false");
            try {
                ps.setInt(1, user.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("PRODUCTID");
                    String name = rs.getString("NAME");
                    int price = rs.getInt("PRICE");
                    Product product = new Product(id, name, price);
                    product.setBasketID(rs.getInt("ID"));
                    list.add(product);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closePrepareStatement(ps);
            }
            return list;
        }

        public synchronized void deleteProductFromBasket (String id){
            PreparedStatement ps = getPrepareStatement("DELETE FROM BASKETS WHERE Id = " + id);
            try {
                ps.executeUpdate();
                logger.info("product " + id + " has been deleted from basket list");
            } catch (SQLException e) {
                logger.info("Fail connect to database");
                e.printStackTrace();
            } finally {
                closePrepareStatement(ps);
            }
        }
    }
