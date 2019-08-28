package app.controller.service;

import app.controller.dao.UserDao;
import app.controller.encrypt.Encrypt;
import app.model.user.User;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
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

    public User getUser(int id) throws SQLException {
        return userDao.getAll().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public User getUserByNickname(String nickname) {
        return userDao.findByNickName(nickname.toUpperCase()).orElseThrow(NoSuchElementException::new);

    }

    public boolean checkBlackList(User user) throws SQLException {
        return Objects.requireNonNull(userDao.getAll().stream().filter(u -> u.getId() == user.getId()).findFirst().orElse(null)).isInBlackList();
    }

    public void addToBlackList(int id) throws SQLException {
        User user = userDao.findById(id);
        user.setInBlackList(true);
        userDao.update(user);
    }

    public void removeFromBlackList(int id) throws SQLException {
        User user = userDao.findById(id);
        user.setInBlackList(false);
        userDao.update(user);
    }

    public boolean authorize(String nickname, String password) {
        String encrypted = Encrypt.encrypt(password, "secret key");
        return userDao.findByNickName(nickname.toUpperCase())
            .filter(user -> user.getPassword().equalsIgnoreCase(encrypted))
            .isPresent();
    }
}
