package app.controller.dao;

import app.model.user.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDaoI extends Dao<User> {

    Optional<User> findByNickName(String nickname) throws SQLException;

}
