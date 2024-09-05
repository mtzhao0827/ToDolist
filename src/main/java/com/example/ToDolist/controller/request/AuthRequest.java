package com.example.ToDolist.controller.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthRequest implements Serializable {
    private static final long serialVersionUID = 32974805623333756L;
    private String username;
    private String password;
}
