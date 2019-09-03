package app.controller.dao;

import app.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class UserDao extends AbstractDao<User> implements UserDaoInterface {

    @Override
    public boolean add(User user) {
        update("INSERT INTO Users (Nickname, Password, Name) Values (?, ?, ?)", preparedStatement -> {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(2, user.getName());
        });
        logger.info("New User " + user.getNickname() + " has been added to database");
        return true;
    }

    @Override
    public List<User> getAll() {
        return getResultList("SELECT * FROM USERS");
    }

    @Override
    public Optional<User> findByNickName(String nickname) {
        return getSingleResult("SELECT * FROM USERS WHERE NICKNAME = ?", preparedStatement -> preparedStatement.setString(1, nickname));
    }

    @Override
    public void update(User user) {
        update("UPDATE USERS SET NICKNAME = ?, PASSWORD = ?, NAME = ?, IS_ADMIN=?, IS_BLOCKED=? WHERE ID = ?", preparedStatement -> {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setBoolean(4, user.isAdministrator());
            preparedStatement.setBoolean(5, user.isInBlackList());
            preparedStatement.setInt(6, user.getId());
        });
        logger.info("user id=" + user.getId() + " has been updated");
    }

    @Override
    public List<User> getBlackList() {
        return getResultList("SELECT * FROM USERS WHERE IS_BLOCKED = ?", preparedStatement ->
                preparedStatement.setBoolean(1, true));
    }

    @Override
    public void delete(User user) {
        update(String.format("DELETE * FROM %s WHERE ID = ?", tableName()), preparedStatement ->
                preparedStatement.setInt(1, user.getId()));
        logger.info("user id=" + user.getId() + " has been deleted");
    }


    @Override
    protected List<User> parseResultSet(ResultSet resultSet) {
        try {
            List<User> result = new LinkedList<>();
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
                result.add(user);
            }
            return result;
        } catch (SQLException e) {
            logger.error("getting list of users fail", e);
            return emptyList();
        }
    }


    @Override
    protected String tableName() {
        return "USERS";
    }
}
