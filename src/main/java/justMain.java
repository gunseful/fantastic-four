import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class justMain {

    public static final String DB_URL = "jdbc:h2:/c:/Users/Ares/IdeaProjects/fantasticFour/db/fantasticFour";
    public static final String DB_Driver = "org.h2.Driver";

    public static void main(String[] args) {

//        try {
//            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
//            Connection connection = DriverManager.getConnection(DB_URL);//соединениесБД
//            System.out.println("Соединение с СУБД выполнено.");
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM TEST");
//            while(resultSet.next()){
//
//                int id = resultSet.getInt(1);
//                String name = resultSet.getString(2);
//                int price = resultSet.getInt(3);
//                System.out.printf("%d. %s - %d \n", id, name, price);
//            }
//            connection.close();       // отключение от БД
//            System.out.println("Отключение от СУБД выполнено.");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace(); // обработка ошибки  Class.forName
//            System.out.println("JDBC драйвер для СУБД не найден!");
//        } catch (SQLException e) {
//            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
//            System.out.println("Ошибка SQL !");
//        }

    }
}
