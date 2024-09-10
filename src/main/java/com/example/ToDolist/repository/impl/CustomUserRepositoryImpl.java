package com.example.ToDolist.repository.impl;

import com.example.ToDolist.exception.user.UserNotFoundException;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.UserRepository;
import com.example.ToDolist.repository.CustomUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository

public class CustomUserRepositoryImpl implements CustomUserRepository {
    @Lazy
    private final UserRepository userRepository;

    @Autowired
    public CustomUserRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByAuthenticatedUser(User authenticatedUser) throws UserNotFoundException {
        var id = authenticatedUser.getId();
        return userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
    }
}
