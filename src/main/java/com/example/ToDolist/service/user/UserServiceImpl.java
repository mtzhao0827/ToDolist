package com.example.ToDolist.service.user;

import com.example.ToDolist.controller.request.AuthRequest;
import com.example.ToDolist.exception.basic.unauthorized.UnauthorizedException;
import com.example.ToDolist.exception.user.UserConflictException;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.ToDoRepository;
import com.example.ToDolist.repository.UserRepository;
import com.example.ToDolist.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User registerUser(User newUser) {
        // 验证用户名是否存在
        User existingUser = userRepository.findByUsername(newUser.getUsername());
        if (existingUser != null){
            String username = newUser.getUsername();
            throw new UserConflictException(username);
        }

        // 密码加密
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public Map<Object, Object> loginUser(AuthRequest authRequest) {
        Map<Object, Object> model = new HashMap<>();

        try {
            String username = authRequest.getUsername();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authRequest.getPassword())
            );
            // authenticationManager 负责认证用户，如果认证成功，将返回一个 authentication 对象，该对象包含已认证的用户信息

            String token = jwtTokenProvider.createToken(authentication);

            model.put("username", username);
            model.put("token", token);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("用户名或密码错误");
        }

        return model;
    }
}
