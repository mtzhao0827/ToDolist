package com.example.ToDolist.exception.user;

import com.example.ToDolist.exception.basic.notfound.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String username)  {
        super("user name " + username + " is not found");
    }
}
