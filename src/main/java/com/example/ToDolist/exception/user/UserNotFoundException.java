package com.example.ToDolist.exception.user;

import com.example.ToDolist.exception.basic.notfound.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id)  {
        super("user ID " + id + " is not found");
    }
}
