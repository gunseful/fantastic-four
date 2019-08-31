package app.controller.service;

import app.model.user.User;

import java.util.List;

public interface UserService {
    List<User> getBlackList();

    boolean addNewUser(User user);

    boolean authorize(String nickname, String password);

    User getUserByNickname(String nickname);

    boolean checkBlackList(User user);

    void addToBlackList(int id);

    void removeFromBlackList(int id);


}
