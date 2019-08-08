package app.model;

import app.entities.products.Basket;
import app.entities.products.Order;
import app.entities.products.Product;
import app.entities.user.User;
import app.model.encrypt.Encrypt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final String DB_URL = "jdbc:h2:/c:/Users/Ares/IdeaProjects/fantasticFour/db/fantasticFour";
    private static final String DB_Driver = "org.h2.Driver";
    private static final Model instance = new Model();
    //делаю потокобезопасный синглтон
    public static synchronized Model getInstance() {
        return instance;
    }

    //This method delete the product with id (which the client gives as attribute) from db
    public void delete(String id) {
        System.out.println("Model deleteProduct");
        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("DELETE FROM PRODUCTS WHERE Id = " + id);
            }
        } catch (Exception exception) {
            System.out.println("Connection failed...");
            System.out.println(exception);
        }
    }

    //This method add the product which you give as attribute to db
    public void add(Product product) {
        System.out.println("Model add");

        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "INSERT INTO PRODUCTS (Name, Price) Values (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, product.getName());
                preparedStatement.setInt(2, product.getPrice());
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    //This method return the User with id that you give as attribute
    public User getUser(int id) {
        System.out.println("Model getUser...");

        try {
            Class.forName(DB_Driver);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Id = ?");
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
                }
                return user;
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            return null;
        }
    }

    //This method return the User with nickname that you give as attribute
    public User getUserByNickName(String nickname) {
        System.out.println("Model getUser...");

        try {
            Class.forName(DB_Driver);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE nickname = ?");
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
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            return null;
        }
    }

    //This method add basket as a string (product ID's with spaces) to User's (that you give as attribute) basket
    public void addToBasket(User user, String basket) {
        System.out.println("Model addToBasket");

        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "UPDATE Users SET basket = ? WHERE id = ?";
                String basketLast = "";
                //checking User's current basket and translates it to a string
                for (Product product : user.getBasket().getList()) {
                    basketLast += product.getId() + " ";
                }
                basketLast += basket;
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, basketLast);
                preparedStatement.setInt(2, user.getId());
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    //This method add new user, that you give as attribute to db
    public boolean addNewUser(User user) {
        System.out.println("Add new user");

        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "INSERT INTO Users (Nickname, Password, Name) Values (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, user.getNickname());
                String password = Encrypt.encrypt(user.getPassword(),"secret key");
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, user.getName());
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            return false;
        }
    }

    //This method checks loggin and password that you write on loggin page with the same in the db
    public boolean checkLogginAndPassword(User user) {
        System.out.println("Model checkLogginAndPassword");

        try {
            Class.forName(DB_Driver);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                //gets all user list with User's nickname that you gave as attribute
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Nickname = ?");
                preparedStatement.setString(1, user.getNickname().toUpperCase());
                ResultSet resultSet = preparedStatement.executeQuery();
                //checks password that you gets from db with User's password, if - true -> getUser with id and make the current User
                resultSet.next();
                String password = Encrypt.encrypt(user.getPassword(), "secret key");
                if (resultSet.getString(3).equals(password)) {
                    int i = resultSet.getInt(1);
                    User currentUser = getUser(i);
                    System.out.println(currentUser);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
        return false;
    }

    public void addUserToBlackList(User user) {
        System.out.println("Model addUserToBlackList");

        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "insert into blacklist select * from users WHERE ID = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, user.getId());
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    //This method making some Order based on the current User's basket
    public void makeOrder(User user) {
        System.out.println("Model makeOrder");

        //convert basket to string
        String order = "";
        for (Product product : user.getBasket().getList()) {
            order += product.getId() + " ";
        }
        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                //Insert String of order, User ID and Date when order was created to Table of Orders in db
                String sql = "INSERT INTO ORDERS (PRODUCTS, CUSTOMERID, CREATEDAT) Values (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, order);
                preparedStatement.setInt(2, user.getId());
                LocalDate todayLocalDate = LocalDate.now();
                preparedStatement.setObject(3, todayLocalDate);
                user.setBasket(new Basket());
                addToBasket(user, "");
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    //This method make Order (in db) as paid
    public void payOrder(int id) {
        System.out.println("Model payOrder");

        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "UPDATE ORDERS SET isPaid = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setBoolean(1, true);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    //This method return list of orders. Current User is Administrator -> return all orders, if not - only orders of current user
    public List<Order> getOrders(User user) {
        System.out.println("Model getOrders");

        List<Order> list = new ArrayList<>();
        try {
            Class.forName(DB_Driver);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                ResultSet resultSet = null;
                if (!user.isAdministrator()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Orders WHERE CustomerID = ?");
                    preparedStatement.setInt(1, user.getId());
                    resultSet = preparedStatement.executeQuery();
                } else {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Orders");
                    resultSet = preparedStatement.executeQuery();
                }
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    LocalDate localDate = resultSet.getObject(4, LocalDate.class);
                    boolean ispaid = resultSet.getBoolean(5);
                    String paid = "";
                    if (ispaid) {
                        paid = "Оплачено";
                    } else {
                        paid = "Неплачено";
                    }
                    String products = resultSet.getString(2);
                    Order order = new Order(id, localDate, new ArrayList<>());
                    order.setPaid(ispaid);
                    order.setCustomerID(resultSet.getInt(3));
                    order.setUser();
                    for (String str : products.split(" ")) {
                        for (Product product : Model.getInstance().getList()) {
                            if (String.valueOf(product.getId()).equals(str)) {
                                order.addProduct(product);
                            }
                        }
                    }
                    list.add(order);
                }
            } catch (Exception ignored) {
            }
        } catch (Exception ignored) {
        }
        return list;
    }

    //This method delete order by id
    public void deleteOrder(int id) {

        System.out.println("Model deleteOrder");
        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("DELETE FROM ORDERS WHERE Id = " + id);
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    //This method return list of all products from db
    public List<Product> getList() {
        System.out.println("Model getList");

        List<Product> list = new ArrayList<>();
        try {
            Class.forName(DB_Driver);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTS");
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    int price = resultSet.getInt(4);
                    list.add(new Product(id, name, price));
                }
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
        return list;
    }


    public boolean checkBlackList(User user) {
        System.out.println("Model checkBlackList");

        try {
            Class.forName(DB_Driver);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM BlackList");
                while (resultSet.next()) {
                    if (resultSet.getInt(1) == user.getId()) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
        return false;
    }

    public List<User> getBlackList() {
        System.out.println("Model getBlackList");

        List<User> list = new ArrayList<>();
        try {
            Class.forName(DB_Driver);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM BLACKLIST");
                User user = null;
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
            }

        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
        return list;
    }

    public void deleteFromBlackList(int id) {
        System.out.println("Model deleteFromBlackList");

        try {
            Class.forName(DB_Driver);
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                Statement statement = conn.createStatement();
                statement.executeUpdate("DELETE FROM blacklist WHERE Id = " + id);
            }
        } catch (Exception exception) {
            System.out.println("Connection failed...");
            System.out.println(exception);
        }
    }
}
