package app.controller.dao;

import app.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDaoInterface extends Dao<User> {

    Optional<User> findByNickName(String nickname);
    List<User> getBlackList();

}
