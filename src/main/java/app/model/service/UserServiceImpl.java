package app.model.service;

import app.entities.user.User;
import app.model.dao.UserDao;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDao();

    public void addNewUser(User user) throws SQLException {
        userDao.add(user);
    }

    public boolean checkBlackList(User user) throws SQLException{

        for(User isBlockedUser : userDao.getAll()){
            if(isBlockedUser.getId() == user.getId()){
                return isBlockedUser.isInBlackList();
            }
        }
        return false;
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
