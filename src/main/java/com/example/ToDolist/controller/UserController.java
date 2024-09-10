package com.example.ToDolist.controller;

import com.example.ToDolist.controller.request.AuthRequest;
import com.example.ToDolist.exception.basic.unauthorized.UnauthorizedException;
import com.example.ToDolist.exception.user.UserBadRequestException;
import com.example.ToDolist.exception.user.UserConflictException;
import com.example.ToDolist.exception.user.UserUnauthorizedException;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.UserRepository;
import com.example.ToDolist.security.JwtTokenProvider;
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
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest)  {
        try {
            String username = authRequest.getUsername();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authRequest.getPassword())
            );
            // authenticationManager 负责认证用户，如果认证成功，将返回一个 authentication 对象，该对象包含已认证的用户信息

            String token = jwtTokenProvider.createToken(authentication);
            Map<Object, Object> model = new HashMap<>();

            model.put("username", username);
            model.put("token", token);

            return ResponseEntity.status(201).body(model);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("用户名或密码错误");
        }
    }
}
