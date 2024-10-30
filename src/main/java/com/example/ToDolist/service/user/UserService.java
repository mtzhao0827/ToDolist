package com.example.ToDolist.service.user;

import com.example.ToDolist.controller.request.AuthRequest;
import com.example.ToDolist.model.User;

import java.util.Map;

public interface UserService {
    User registerUser( User newUser);
    Map<Object, Object> loginUser(AuthRequest authRequest);
}
