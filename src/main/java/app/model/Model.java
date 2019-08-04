package app.model;

import app.entities.Basket;
import app.entities.Product;
import app.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public static final String DB_URL = "jdbc:h2:/c:/Users/Ares/IdeaProjects/fantasticFour/db/fantasticFour";
    public static final String DB_Driver = "org.h2.Driver";
    private static final Model instance = new Model();

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private List<User> model;

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        model = new ArrayList<>();
    }

    public void delete(String id){

        try{

            Class.forName(DB_Driver);

            try (Connection conn = DriverManager.getConnection(DB_URL)){

                Statement statement = conn.createStatement();

                int rows = statement.executeUpdate("DELETE FROM PRODUCTS WHERE Id = "+id);
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public void add(Product product) {

        try{

            Class.forName(DB_Driver);

            try (Connection conn = DriverManager.getConnection(DB_URL)){

                String sql = "INSERT INTO PRODUCTS (Name, Price) Values (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, product.getName());
                preparedStatement.setInt(2, product.getPrice());

                preparedStatement.executeUpdate();

            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public User getUser(int id){

        try{
            Class.forName(DB_Driver);

            try (Connection connection = DriverManager.getConnection(DB_URL)){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Id = ?");
                preparedStatement.setInt(1, id);


                ResultSet resultSet = preparedStatement.executeQuery();

                User user = new User();
                while(resultSet.next()){
                        user.setName(resultSet.getString(4));
                        user.setNickname(resultSet.getString(2));
                        user.setAdministrator(resultSet.getBoolean(5));
                        user.setId(resultSet.getInt(1));
                        user.setPassword(resultSet.getString(3));

                        Basket basket = new Basket();
                        if(resultSet.getString(6) != null) {
                            for (String str : resultSet.getString(6).split(" ")) {
                                System.out.println(resultSet.getString(6));
                                for (Product product : Model.getInstance().getList()) {
                                    if (str.trim().equals(String.valueOf(product.getId()))) {
                                        user.basketList.add(product);
                                    }
                                }
                            }
                        }

                        user.setBasket(basket);

                    }
                return user;
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");
            System.out.println(ex);
            return null;


        }



    }

    public void addToBasket(User user, String basket){
        try{

            Class.forName(DB_Driver);

            try (Connection conn = DriverManager.getConnection(DB_URL)){

                String sql = "UPDATE Users SET basket = ? WHERE id = ?";

                System.out.println("add to basket 1");

                String basketLast = "";
                for(Product product : user.basketList){
                    basketLast+=product.getId()+" ";
                }

                basketLast+=basket;


                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, basketLast);
                preparedStatement.setInt(2, user.getId());

                preparedStatement.executeUpdate();
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");
            System.out.println(ex);
        }




    }

        public boolean addNewUser(User user) {

        try{

            Class.forName(DB_Driver);

            try (Connection conn = DriverManager.getConnection(DB_URL)){

                String sql = "INSERT INTO Users (Nickname, Password, Name) Values (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, user.getNickname());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());

                preparedStatement.executeUpdate();
                return true;

            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");
            System.out.println(ex);
            return false;


        }


    }

    public boolean checkLogginAndPassword(User user){
        try{
            Class.forName(DB_Driver);

            try (Connection connection = DriverManager.getConnection(DB_URL)){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Nickname = ?");
                preparedStatement.setString(1, user.getNickname().toUpperCase());


                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    if(resultSet.getString(3).equals(user.getPassword())){
                        int i = resultSet.getInt(1);
                        currentUser = getUser(i);

                        System.out.println(currentUser);


                        return true;
                    }else{
                        return false;
                    }

                }
                connection.close();
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
        return false;


    }

    public List<Product> getList() {
      List<Product> list = new ArrayList<>();
        try{
            Class.forName(DB_Driver);

            try (Connection connection = DriverManager.getConnection(DB_URL)){

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCTS");
                while(resultSet.next()){

                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    int price = resultSet.getInt(4);
                    String str = id+" "+ name;

                    list.add(new Product(id, name, price));
                }
                connection.close();
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }

        return list;
    }

}
