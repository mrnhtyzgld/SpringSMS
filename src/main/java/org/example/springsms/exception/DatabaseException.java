package org.example.springsms.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
        // FIXME parametrelerle oyna
    }

    public DatabaseException() {
        super();
    }
}
