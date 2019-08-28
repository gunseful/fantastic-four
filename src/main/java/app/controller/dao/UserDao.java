package app.controller.dao;

import app.controller.encrypt.Encrypt;
import app.model.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class UserDao extends AbstractDao<User> implements UserDaoI {

    @Override
    public boolean add(User user) throws SQLException {
        String password = Encrypt.encrypt(user.getPassword(), "secret key");
        sql = "INSERT INTO Users (Nickname, Password, Name) Values ('" + user.getNickname() + "', '" + password + "', '" + user.getName() + "')";
        getPreparedStatement().executeUpdate();
        logger.info("New User " + user.getNickname() + " has been added to database");
        return true;
    }

    @Override
    public List<User> getAll() throws SQLException {
        sql = "SELECT * FROM USERS";
        ResultSet resultSet = getPreparedStatement().executeQuery();
        return parseResultSet(resultSet);
    }

    @Override
    public Optional<User> findByNickName(String nickname) {
        //todo I would add another method which accepts String sql and collection of parameters to be injected
        return getSingleResult(String.format("SELECT * FROM USERS WHERE NICKNAME = %s", nickname));
    }

    @Override
    public void update(User user) throws SQLException {
        sql = "UPDATE USERS SET NICKNAME = ?, PASSWORD = ?, NAME = ?, IS_ADMIN=?, IS_BLOCKED=? WHERE ID =" + user.getId();
        PreparedStatement ps = getPreparedStatement();
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
        getPreparedStatement().executeUpdate();
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
            //todo logger
            return emptyList();
        }
    }

    @Override
    protected String tableName() {
        return "USERS";
    }
}
