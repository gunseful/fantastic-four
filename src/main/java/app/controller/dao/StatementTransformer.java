package app.controller.dao;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface StatementTransformer<T> extends Consumer<T> {

    @Override
    default void accept(T t) {
        try {
            transform(t);
        } catch (SQLException e) {
            //todo create custom exception
            throw new RuntimeException(e);
        }
    }

    void transform(T t) throws SQLException;
}
