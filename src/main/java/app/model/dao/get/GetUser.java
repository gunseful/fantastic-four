package app.model.dao.get;

import app.entities.user.User;
import app.model.dao.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUser extends AbstractDao {


    public GetUser(int id) {
        sql = "SELECT * FROM Users WHERE ID = "+id;
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        //создаем объект юзер и добавляем в него данные из базы данных
        User user = new User();
        while (resultSet.next()) {
            user.setId(resultSet.getInt("ID"));
            user.setNickname(resultSet.getString("NICKNAME"));
            user.setPassword(resultSet.getString("PASSWORD"));
            user.setName(resultSet.getString("NAME"));
            user.setAdministrator(resultSet.getBoolean("IS_ADMIN"));
            user.setInBlackList(resultSet.getBoolean("IS_BLOCKED"));
        }
        return user;
    }
}
