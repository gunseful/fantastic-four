package app.controller.service;

import app.model.user.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    List<User> getBlackList() throws SQLException;

    boolean addNewUser(User user) throws SQLException;

    boolean checkLogginAndPassword(User user) throws SQLException;

    User getUser(int id) throws SQLException;

    User getUserByNickname(String nickname) throws SQLException;

    boolean checkBlackList(User user) throws SQLException;

    void addToBlackList(int id) throws SQLException;

    void removeFromBlackList(int id) throws SQLException;


}
