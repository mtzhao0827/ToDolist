package com.example.ToDolist.exception.basic.badrequest;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

}
