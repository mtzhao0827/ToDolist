package com.example.ToDolist.controller;

import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //注册新用户
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User newUser){
        //还要处理用户名是否存在，密码加密等
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(newUser));
    }

    //用户登录
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user){
        //还要验证密码
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())){
            return new ResponseEntity<>(existingUser, HttpStatus.OK); //还要修改
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
