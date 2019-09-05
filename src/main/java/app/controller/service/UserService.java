package app.controller.service;

import app.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getBlackList();

    boolean addNewUser(User user);

    Optional<User> authorize(String nickname, String password);

    User getUserByNickname(String nickname);

    boolean checkBlackList(User user);

    void addToBlackList(int id);

    void removeFromBlackList(int id);


}
