package app.controller.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    boolean add(T t) throws SQLException;

    T findById(int id);

    List<T> getAll() throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;


}
