package com.example.ToDolist.controller;

import com.example.ToDolist.exception.user.UserBadRequestException;
import com.example.ToDolist.exception.user.UserConflictException;
import com.example.ToDolist.exception.user.UserUnauthorizedException;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 注册新用户
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        // 验证用户名是否存在
        User existingUser = userRepository.findByUsername(newUser.getUsername());
        if (existingUser != null){
            String username = newUser.getUsername();
            throw new UserConflictException(username);
        }

        // 密码加密
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
    }

    // 用户登录
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user)  {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser == null){
            String username = user.getUsername();
            throw new UserBadRequestException(username);
        }

        // 还要验证密码
//        if (!existingUser.getPassword().equals(user.getPassword())) {
//            Long id = existingUser.getId();
//            throw new UserUnauthorizedException(id);
//        }
        if (!passwordEncoder.matches(user.getPassword(),existingUser.getPassword())) {
            Long id = existingUser.getId();
            throw new UserUnauthorizedException(id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(existingUser);
    }
}
