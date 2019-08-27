package app.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    boolean add(T t) throws SQLException;
    T read(int id) throws SQLException;
    List<T> getAll() throws SQLException;
    void update(T t) throws SQLException;
    void delete(int id) throws SQLException;


}
