package app.model.controller;

import app.entities.products.Basket;
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
                int price = rs.getInt(4);
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
    public synchronized boolean checkBlackList(User user) {
        String sql = "SELECT * FROM BLACKLIST";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    if (resultSet.getInt(1) == user.getId()) {
                        return true;
                    }
                }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            ex.printStackTrace();
        }finally {
            closePrepareStatement(preparedStatement);
        }
        return false;
    }

    @Override
    public synchronized List<User> getBlackList() {
        List<User> list = new ArrayList<>();
        PreparedStatement ps = getPrepareStatement("SELECT * FROM BLACKLIST");
        try {
            ResultSet resultSet = ps.executeQuery();
            User user;
            while (resultSet.next()) {
                user = new User();
                int id = resultSet.getInt(1);
                user.setId(id);
                String nickname = resultSet.getString(2);
                user.setNickname(nickname);
                String password = resultSet.getString(3);
                user.setPassword(password);
                String name = resultSet.getString(4);
                user.setName(name);
                boolean isAdmin = resultSet.getBoolean(5);
                user.setAdministrator(isAdmin);
                user.setBasket(new Basket());
                //This part of method is checking db for User basket, gets String with product ID's and
                //check does the current list of product has this product
                //if does - add to User's basket.
                if (resultSet.getString(6) != null) {
                    for (String str : resultSet.getString(6).split(" ")) {
                        for (Product product : getList()) {
                            if (str.trim().equals(String.valueOf(product.getId()))) {
                                user.getBasket().add(product);
                            }
                        }
                    }
                }
                list.add(user);
            }

        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }finally {
            closePrepareStatement(ps);
        }
        return list;
    }

    @Override
    public synchronized void deleteFromBlackList(int id) {
        PreparedStatement ps = getPrepareStatement("DELETE FROM BLACKLIST WHERE Id = " + id);
        try {
            ps.executeUpdate();
            logger.info("user "+id+" has been deleted from black list");
        } catch (SQLException e) {
            logger.info("Fail connect to database");
            System.out.println("Connection failed...");
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }
    }

    @Override
    public synchronized void deleteProduct(String id) {
        PreparedStatement ps = getPrepareStatement("DELETE FROM PRODUCTS WHERE Id = " + id);
        try {
            ps.executeUpdate();
            logger.info("product "+id+" has been deleted from product list");
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
            logger.info("product "+product.getName()+" has been added to product list");
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
                user.setBasket(new Basket());
                //This part of method is checking db for User basket, gets String with product ID's and
                //check does the current list of product has this product
                //if does - add to User's basket.
                if (resultSet.getString(6) != null) {
                    for (String str : resultSet.getString(6).split(" ")) {
                        for (Product product : getList()) {
                            if (str.trim().equals(String.valueOf(product.getId()))) {
                                user.getBasket().add(product);
                            }
                        }
                    }
                }
                if(checkBlackList(user)){
                    user.setInBlackList(true);}
                else {user.setInBlackList(false);
                }
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
                user.setBasket(new Basket());
                //This part of method is checking db for User basket, gets String with product ID's and
                //check does the current list of product has this product
                //if does - add to User's basket.
                if (resultSet.getString(6) != null) {
                    for (String str : resultSet.getString(6).split(" ")) {
                        for (Product product : getList()) {
                            if (str.trim().equals(String.valueOf(product.getId()))) {
                                user.getBasket().add(product);
                            }
                        }
                    }
                }
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
    public synchronized void addToBasket(User user, String basket) {
        String sql = "UPDATE Users SET basket = ? WHERE id = ?";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            String basketLast = "";
            //checking User's current basket and translates it to a string
            for (Product product : user.getBasket().getList()) {
                basketLast += product.getId() + " ";
            }
            basketLast += basket;
            preparedStatement.setString(1, basketLast);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
            logger.info("User="+user.getNickname()+" added "+basket+" products to his basket");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
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
            logger.info("New User "+user.getNickname()+" has been added to database");
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
            if(resultSet.next()){
                return true;

            }else{
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
    public synchronized void addUserToBlackList(User user) {
        String sql = "insert into blacklist select * from users WHERE ID = ?";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
            logger.info("User= "+user.getNickname()+" was added to blacklist");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized void makeOrder(User user) {
        String order = "";
        for (Product product : user.getBasket().getList()) {
            order += product.getId() + " ";
        }
        String sql = "INSERT INTO ORDERS (PRODUCTS, CUSTOMERID, CREATEDAT) Values (?, ?, ?)";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            //Insert String of order, User ID and Date when order was created to Table of Orders in db
            preparedStatement.setString(1, order);
            preparedStatement.setInt(2, user.getId());
            LocalDate creationDate = LocalDate.now();
            preparedStatement.setObject(3, creationDate);
            user.setBasket(new Basket());
            addToBasket(user, "");
            preparedStatement.executeUpdate();
            logger.info("order was added to database");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized void payOrder(int id) {
        String sql = "UPDATE ORDERS SET isPaid = ? WHERE id = ?";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        try {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            logger.info("order was paid");
        } catch (Exception ex) {
            logger.info("Fail connect to database");
            ex.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement);
        }
    }

    @Override
    public synchronized List<Order> getOrders(User user) {
        String sql = (!user.isAdministrator()) ? "SELECT * FROM Orders WHERE CustomerID = ?" : "SELECT * FROM Orders";
        PreparedStatement preparedStatement = getPrepareStatement(sql);
        List<Order> list = new ArrayList<>();
        try {
            if (!user.isAdministrator()) {
                preparedStatement.setInt(1, user.getId());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                LocalDate localDate = resultSet.getObject(4, LocalDate.class);
                boolean ispaid = resultSet.getBoolean(5);
                String products = resultSet.getString(2);
                Order order = new Order(id, localDate, new ArrayList<>());
                order.setPaid(ispaid);
                order.setCustomerID(resultSet.getInt(3));
                order.setUser(getUser(resultSet.getInt(3)));
                for (String str : products.split(" ")) {
                    for (Product product : getList()) {
                        if (String.valueOf(product.getId()).equals(str)) {
                            order.addProduct(product);
                        }
                    }
                }
                list.add(order);
            }
        } catch (Exception e) {
            logger.info("Fail connect to database");
            e.printStackTrace();
        } finally {
            closePrepareStatement(preparedStatement);
        }
        return list;
    }

    @Override
    public synchronized void deleteOrder(int id) {
        PreparedStatement ps = getPrepareStatement("DELETE FROM ORDERS WHERE Id = " + id);
        try {
            ps.executeUpdate();
            logger.info("Order= "+id+" was deleted from database");
        } catch (SQLException e) {
            logger.info("Fail connect to database");
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }
    }
}
