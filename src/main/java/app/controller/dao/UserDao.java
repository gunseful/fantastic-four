package app.controller.dao;

import app.controller.encrypt.Encrypt;
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
        if(findByNickName(user.getNickname().toUpperCase()).isPresent()){return false;}
        saveOrUpdate("INSERT INTO Users (Nickname, Password, Name) Values (?, ?, ?)", preparedStatement -> {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, Encrypt.encrypt(user.getPassword(), "secret key"));
            preparedStatement.setString(3, user.getName());
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
        saveOrUpdate("UPDATE USERS SET NICKNAME = ?, PASSWORD = ?, NAME = ?, ROLE=? WHERE ID = ?", preparedStatement -> {
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setInt(5, user.getId());
        });
        logger.info("user id={} has been updated", user.getId());
    }

    @Override
    public List<User> getBlackList() {
        return getResultList("SELECT * FROM USERS WHERE ROLE = ?", preparedStatement ->
                preparedStatement.setString(1, "BLOCKED"));
    }

    @Override
    public void delete(User user) {
        saveOrUpdate(String.format("DELETE * FROM %s WHERE ID = ?", tableName()), preparedStatement ->
                preparedStatement.setInt(1, user.getId()));
        logger.info("user id={} has been deleted", user.getId());
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
                String role = resultSet.getString("ROLE");
                user.setRole(role);
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
