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

public class UserDao extends AbstractDao<User> implements UserDaoI {

    @Override
    public boolean add(User user) throws SQLException {
        getPreparedStatement(
                String.format("INSERT INTO Users (Nickname, Password, Name) Values ('%s', '%s', '%s')",
                        user.getNickname(),
                        Encrypt.encrypt(user.getPassword(), "secret key"),
                        user.getName()))
                .executeUpdate();
        logger.info("New User " + user.getNickname() + " has been added to database");
        return true;
    }



    @Override
    public List<User> getAll() throws SQLException {
        ResultSet resultSet = getPreparedStatement("SELECT * FROM USERS").executeQuery();
        return parseResultSet(resultSet);
    }

    @Override
    public Optional<User> findByNickName(String nickname) {
        //todo I would add another method which accepts String sql and collection of parameters to be injected
        return getSingleResult(String.format("SELECT * FROM USERS WHERE NICKNAME = '%s'", nickname));
    }


    @Override
    public void update(User user) throws SQLException {
        PreparedStatement ps = getPreparedStatement(String.format("UPDATE USERS SET NICKNAME = '%s', PASSWORD = '%s', NAME = '%s', IS_ADMIN=?, IS_BLOCKED=? WHERE ID = %d",
                user.getNickname(), user.getPassword(), user.getName(), user.getId()));
        ps.setBoolean(1, user.isAdministrator());
        ps.setBoolean(2, user.isInBlackList());
        ps.executeUpdate();
    }

    @Override
    public void delete(User user) throws SQLException {
        getPreparedStatement(String.format("DELETE * FROM %s WHERE ID = %s", tableName(), user.getId())).executeUpdate();
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
            logger.error(e);
            return emptyList();
        }
    }


    @Override
    protected String tableName() {
        return "USERS";
    }
}
