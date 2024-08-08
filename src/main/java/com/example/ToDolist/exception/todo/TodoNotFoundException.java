package com.example.ToDolist.exception.todo;

import com.example.ToDolist.exception.basic.notfound.NotFoundException;

public class TodoNotFoundException extends NotFoundException {
    public TodoNotFoundException(Long id) {
        super("todo ID " + id + " is not found");
    }
}
