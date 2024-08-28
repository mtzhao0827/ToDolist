package com.example.ToDolist.exception.user;

import com.example.ToDolist.exception.basic.badrequest.BadRequestException;

public class UserBadRequestException extends BadRequestException {
    public UserBadRequestException(String username) {
        super("用户名" + username + "不存在");
    }
}
