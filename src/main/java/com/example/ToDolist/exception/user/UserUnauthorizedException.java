package com.example.ToDolist.exception.user;

import com.example.ToDolist.exception.basic.unauthorized.UnauthorizedException;

public class UserUnauthorizedException extends UnauthorizedException {
    public UserUnauthorizedException(Long id){
        super("user ID " + id + " is unauthorized.");
    }
}
