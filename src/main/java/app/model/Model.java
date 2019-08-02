package app.model;

import app.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public static final String DB_URL = "jdbc:h2:/c:/Users/Ares/IdeaProjects/fantasticFour/db/fantasticFour";
    public static final String DB_Driver = "org.h2.Driver";
    private static Model instance = new Model();

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

                int rows = statement.executeUpdate("DELETE FROM TEST WHERE Id = "+id);
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
    }

    public void add(User user) {

        try{

            Class.forName(DB_Driver);


            String name = user.getName();
            int dick = user.getAge();

            try (Connection conn = DriverManager.getConnection(DB_URL)){

                String sql = "INSERT INTO TEST (Name, Dick) Values (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, dick);

                preparedStatement.executeUpdate();

            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...");

            System.out.println(ex);
        }


    }

    public List<User> getList() {
      List<User> list = new ArrayList<>();
        try{
            Class.forName(DB_Driver);

            try (Connection connection = DriverManager.getConnection(DB_URL)){

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM TEST");
                while(resultSet.next()){

                    int id = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    int dick = resultSet.getInt(3);
                    String str = id+" "+ name+" - "+dick;

                    list.add(new User(id, name, dick));
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
