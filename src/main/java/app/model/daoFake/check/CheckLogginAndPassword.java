package app.model.daoFake.check;

import app.entities.user.User;
import app.model.daoFake.abstraction.AbstractDao;
import app.model.encrypt.Encrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckLogginAndPassword extends AbstractDao {

    private String password;
    private String nickname;
    public CheckLogginAndPassword(User user) {
        password = Encrypt.encrypt(user.getPassword(), "secret key");
        nickname = user.getNickname();
        sql = "SELECT * FROM Users WHERE NICKNAME = ? AND PASSWORD = ?";
    }

    @Override
    public Object preparedStatment() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nickname.toUpperCase());
        preparedStatement.setString(2, password);
        //ищем в базе данных ник и пароль совпадения если есть хоть одно (а больше и не может быть) то кидаем тру, если не нашли то фелс
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
