package com.example.Library.exception;

public class AuthorIsAssignedToBookException extends RuntimeException {

    public AuthorIsAssignedToBookException() {
    }

    public AuthorIsAssignedToBookException(String message) {
        super(message);
    }

    public AuthorIsAssignedToBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorIsAssignedToBookException(Throwable cause) {
        super(cause);
    }

    public AuthorIsAssignedToBookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
