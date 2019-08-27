package app.model.daoFake.get;

import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserByNickName extends AbstractDao {

    private User user;
    private int productId;

    public GetUserByNickName(String nickname) {
        sql = "SELECT * FROM Users WHERE nickname = '" + nickname+"'";
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = new User();
        resultSet.next();
        user.setId(resultSet.getInt("ID"));
        user.setNickname(resultSet.getString("NICKNAME"));
        user.setPassword(resultSet.getString("PASSWORD"));
        user.setName(resultSet.getString("NAME"));
        user.setAdministrator(resultSet.getBoolean("IS_ADMIN"));
        return user;
}
}
