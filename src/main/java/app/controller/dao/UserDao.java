package app.controller.dao;

import app.model.user.User;
import app.controller.encrypt.Encrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends DaoImpl<User> {

    @Override
    public boolean add(User user) throws SQLException {
        String password = Encrypt.encrypt(user.getPassword(), "secret key");
        sql = "INSERT INTO Users (Nickname, Password, Name) Values ('" + user.getNickname() + "', '" + password + "', '" + user.getName() + "')";
        start().executeUpdate();
        logger.info("New User " + user.getNickname() + " has been added to database");
        return true;
    }

    @Override
    public List<User> getAll() throws SQLException {
        sql = "SELECT * FROM USERS";
        List<User> list = new ArrayList<>();
        ResultSet resultSet = start().executeQuery();
        User user;
        while (resultSet.next()) {
            user = new User();
            int id = resultSet.getInt("ID");
            user.setId(id);
            String nickname = resultSet.getString("NICKNAME");
            user.setNickname(nickname);
            String password = resultSet.getString("PASSWORD");
            user.setPassword(password);
            String name = resultSet.getString("NAME");
            user.setName(name);
            boolean isAdmin = resultSet.getBoolean("IS_ADMIN");
            user.setAdministrator(isAdmin);
            boolean isBlocked = resultSet.getBoolean("IS_BLOCKED");
            user.setInBlackList(isBlocked);
            list.add(user);
        }
        return list;
    }

    @Override
    public User read(int id) throws SQLException {
        sql = "SELECT * FROM USERS WHERE ID = " + id;
        ResultSet resultSet = start().executeQuery();
        User user;
        resultSet.next();
        user = new User();
        user.setId(id);
        String nickname = resultSet.getString("NICKNAME");
        user.setNickname(nickname);
        String password = resultSet.getString("PASSWORD");
        user.setPassword(password);
        String name = resultSet.getString("NAME");
        user.setName(name);
        boolean isAdmin = resultSet.getBoolean("IS_ADMIN");
        user.setAdministrator(isAdmin);
        user.setInBlackList(resultSet.getBoolean("IS_BLOCKED"));
        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        sql = "UPDATE USERS SET NICKNAME = ?, PASSWORD = ?, NAME = ?, IS_ADMIN=?, IS_BLOCKED=? WHERE ID ="+user.getId();
        PreparedStatement ps = start();
        ps.setString(1, user.getNickname());
//        String password = Encrypt.encrypt(user.getPassword(), "secret key");
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getName());
        ps.setBoolean(4, user.isAdministrator());
        ps.setBoolean(5, user.isInBlackList());
        ps.executeUpdate();
    }

    @Override
    public void delete(User user) throws SQLException {
        sql = "DELETE * FROM USERS WHERE ID = " + user.getId();
        start().executeUpdate();
    }
}
