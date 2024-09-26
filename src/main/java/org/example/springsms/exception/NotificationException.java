package org.example.springsms.exception;

public class NotificationException extends RuntimeException {
    public NotificationException(String dataType) {
        super(dataType + " is invalid.");
        // FIXME parametrelerle oyna
    }
}
