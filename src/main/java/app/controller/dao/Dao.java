package app.controller.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    boolean add(T t) throws SQLException;

    T findById(int id);

    List<T> getAll();

    void update(T t);

    void delete(T t);

    List<T> findBy(String parametersForSearch);

    Optional<T> singleFindBy(String parametersForSearch);

}
