package com.example.ToDolist.exception.user;

import com.example.ToDolist.exception.basic.conflict.ConflictException;

public class UserConflictException extends ConflictException {
    public UserConflictException(String username) {
        super("用户名" + username + "已被使用");
    }
}
