package org.example.exceptions;

public class InvalidFiniteAutomatonException extends RuntimeException{
    public InvalidFiniteAutomatonException() {
        super();
    }

    public InvalidFiniteAutomatonException(String message) {
        super(message);
    }
}
