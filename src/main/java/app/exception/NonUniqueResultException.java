package app.exception;

public class NonUniqueResultException extends RuntimeException {

    public NonUniqueResultException(String message) {
        super(message);
    }
}
