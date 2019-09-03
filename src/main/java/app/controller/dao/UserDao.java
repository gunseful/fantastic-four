package app.controller.dao;

import app.controller.encrypt.Encrypt;
import app.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class UserDao extends AbstractDao<User> implements UserDaoInterface {

    @Override
    public boolean add(User user) {
        try {
            getPreparedStatement(
                    String.format("INSERT INTO Users (Nickname, Password, Name) Values ('%s', '%s', '%s')",
                            user.getNickname(),
                            Encrypt.encrypt(user.getPassword(), "secret key"),
                            user.getName()))
                    .executeUpdate();
            logger.info("New User " + user.getNickname() + " has been added to database");
            return true;
        } catch (SQLException e) {
            logger.error("adding user fail", e);
            return false;
        }
    }

    @Override
    public List<User> getAll() {
        try {
            ResultSet resultSet = getPreparedStatement("SELECT * FROM USERS").executeQuery();
            return parseResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("selecting users fail", e);
            return emptyList();
        }
    }

    @Override
    public Optional<User> findByNickName(String nickname) {
        return singleFindBy(String.format("NICKNAME = '%s'", nickname));
    }

    @Override
    public void update(User user) {
        try {
            //fixme I would rather use type-safe `statement.setString() | statement.setInt(), than String.format() because it is not type safe`
            //It seems really stragne that half parameters is set through String.format(), and others are set through statement.setBoolean()
            PreparedStatement ps = getPreparedStatement(String.format("UPDATE USERS SET NICKNAME = '%s', PASSWORD = '%s', NAME = '%s', IS_ADMIN=?, IS_BLOCKED=? WHERE ID = %d",
                    user.getNickname(), user.getPassword(), user.getName(), user.getId()));
            ps.setBoolean(1, user.isAdministrator());
            ps.setBoolean(2, user.isInBlackList());
            ps.executeUpdate();
            logger.info("user id="+user.getId()+" has been updated");
        } catch (SQLException e) {
            logger.error("updating user fail");
        }
    }

    @Override
    public List<User> getBlackList(){
        return getResultList("SELECT * FROM USERS WHERE IS_BLOCKED = ?", preparedStatement -> preparedStatement.setBoolean(1, true));
    }

    @Override
    public void delete(User user) {
        try {
            getPreparedStatement(String.format("DELETE * FROM %s WHERE ID = %s", tableName(), user.getId())).executeUpdate();
            logger.info("user id="+user.getId()+" has been deleted");
        } catch (SQLException e) {
            logger.error("deleting user fail", e);
        }
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
            logger.error("getting list of users fail",e);
            return emptyList();
        }
    }


    @Override
    protected String tableName() {
        return "USERS";
    }
}
