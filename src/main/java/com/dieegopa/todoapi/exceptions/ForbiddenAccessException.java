package com.dieegopa.todoapi.exceptions;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException() {
        super("You do not have access to this resource");
    }
}
