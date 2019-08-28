package app.controller.service;

import app.controller.dao.UserDao;
import app.controller.encrypt.Encrypt;
import app.model.user.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDao();

    public List<User> getBlackList() throws SQLException {
        return userDao.getAll().stream().filter(User::isInBlackList).collect(Collectors.toList());
    }

    public boolean addNewUser(User user) throws SQLException {
        userDao.add(user);
        return true;
    }

    public boolean checkLogginAndPassword(User user) throws SQLException {
        String password = Encrypt.encrypt(user.getPassword(), "secret key");
        String nickname = user.getNickname().toUpperCase();
        return userDao.getAll().stream().anyMatch(u -> u.getNickname().equals(nickname) && u.getPassword().equals(password));
    }

    public User getUser(int id) throws SQLException {
        return userDao.getAll().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public User getUserByNickname(String nickname) throws SQLException {
        return userDao.getAll().stream().filter(u -> u.getNickname().equals(nickname)).findFirst().orElse(null);

    }

    public boolean checkBlackList(User user) throws SQLException {
        return Objects.requireNonNull(userDao.getAll().stream().filter(u -> u.getId() == user.getId()).findFirst().orElse(null)).isInBlackList();
    }

    public void addToBlackList(int id) throws SQLException {
        User user = userDao.read(id);
        user.setInBlackList(true);
        userDao.update(user);
    }

    public void removeFromBlackList(int id) throws SQLException {
        User user = userDao.read(id);
        user.setInBlackList(false);
        userDao.update(user);
    }
}
