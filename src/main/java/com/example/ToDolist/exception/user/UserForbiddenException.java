package com.example.ToDolist.exception.user;


import com.example.ToDolist.exception.basic.forbidden.ForbiddenException;

public class UserForbiddenException extends ForbiddenException {
    public UserForbiddenException() {
        super("你只能修改自己的todo！");
    }
}

