package app.model.daoFake.check;

import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckBlackList extends AbstractDao {

    public CheckBlackList(User user) {
        sql = "SELECT * FROM USERS WHERE ID = " + user.getId();
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getBoolean("IS_BLOCKED");
    }
}
