package com.example.ToDolist.controller;

import com.example.ToDolist.controller.request.AuthRequest;
import com.example.ToDolist.exception.basic.unauthorized.UnauthorizedException;
import com.example.ToDolist.exception.user.UserBadRequestException;
import com.example.ToDolist.exception.user.UserConflictException;
import com.example.ToDolist.exception.user.UserUnauthorizedException;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.UserRepository;
import com.example.ToDolist.security.JwtTokenProvider;
import com.example.ToDolist.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UserController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    // 注册新用户
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(newUser));
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest)  {
        return ResponseEntity.status(201).body(userService.loginUser(authRequest));
    }
}
