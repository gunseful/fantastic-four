package app.controller.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    boolean add(T t) throws SQLException;

    T findById(int id);

    List<T> findBy(String parametersForSearch);
    Optional<T> singleFindBy(String parametersForSearch);

    List<T> getAll() throws SQLException;

    void update(T t) throws SQLException;

    void delete(T t) throws SQLException;


}
