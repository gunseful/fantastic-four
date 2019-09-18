package app.controller.service;

import app.controller.dao.UserDao;
import app.controller.encrypt.Encrypt;
import app.enums.Roles;
import app.model.user.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDao();

    @Override
    public List<User> getBlackList() {
        return userDao.getBlackList();
    }

    @Override
    public boolean addNewUser(User user) {
        return userDao.add(user);
    }

    @Override
    public boolean checkBlackList(User user) {
        return userDao.findById(user.getId()).orElseThrow(IllegalStateException::new).getRole().equals(Roles.BLOCKED.name());
    }

    @Override
    public void addToBlackList(int id) {
        User user = userDao.findById(id).orElseThrow(IllegalStateException::new);
        user.setRole(Roles.BLOCKED.name());
        userDao.update(user);
    }

    @Override
    public void removeFromBlackList(int id) {
        User user = userDao.findById(id).orElseThrow(IllegalStateException::new);
        user.setRole(Roles.USER.name());
        userDao.update(user);
    }

    @Override
    public Optional<User> authorize(String nickname, String password) {
        String encrypted = Encrypt.encrypt(password, "secret key");
        return userDao.findByNickName(nickname.toUpperCase())
                .filter(user -> user.getPassword().equalsIgnoreCase(encrypted));
    }
}
