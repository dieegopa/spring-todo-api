package com.dieegopa.todoapi.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("User already exists with the provided email.");
    }
}
