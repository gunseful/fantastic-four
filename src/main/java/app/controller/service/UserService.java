package app.controller.service;

import app.model.user.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public List<User> getBlackList() throws SQLException;

    public boolean addNewUser(User user) throws SQLException;

    public boolean checkLogginAndPassword(User user) throws SQLException;

    public User getUser(int id) throws SQLException;

    public User getUserByNickname(String nickname) throws SQLException;

    public boolean checkBlackList(User user) throws SQLException;

    public void addToBlackList(int id) throws SQLException;

    public void removeFromBlackList(int id) throws SQLException;


}
