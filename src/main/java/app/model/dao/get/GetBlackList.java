package app.model.dao.get;

import app.entities.user.User;
import app.model.dao.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetBlackList extends AbstractDao {

    public GetBlackList() {
        sql = "SELECT * FROM USERS WHERE IS_BLOCKED = TRUE";
    }

    @Override
    public Object preparedStatment() throws SQLException {
        List<User> list = new ArrayList<>();
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
            boolean isAdmin = resultSet.getBoolean("IS_ADMIN");
            user.setAdministrator(isAdmin);
            list.add(user);
        }
        return list;
    }
}
