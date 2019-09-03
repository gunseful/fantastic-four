package app.controller.dao;

import app.exception.TransformException;

import java.sql.SQLException;
import java.util.function.Consumer;

@FunctionalInterface
public interface StatementTransformer<T> extends Consumer<T> {


    @Override
    default void accept(T t) {
        try {
            transform(t);
        } catch (SQLException e) {
            throw new TransformException(
                    "Could not transform SQL request");
        }
    }

    void transform(T t) throws SQLException;
}
