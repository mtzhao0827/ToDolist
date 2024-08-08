package com.example.ToDolist.exception;

public class ExceptionResponseBody {
    private String message;
    public ExceptionResponseBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
