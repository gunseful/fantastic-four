package app.model.daoFake.add;

import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;
import app.model.encrypt.Encrypt;

import java.sql.SQLException;

public class AddNewUser extends AbstractDao {
    private User user;

    public AddNewUser(User user) {
        this.user = user;
    }
    @Override
    public Object preparedStatment() throws SQLException {
        String password = Encrypt.encrypt(user.getPassword(), "secret key");
        sql="INSERT INTO Users (Nickname, Password, Name) Values ('" + user.getNickname() + "', '" + password + "', '" + user.getName() + "')";
        logger.info("New User " + user.getNickname() + " has been added to database");
        return true;
    }
}
