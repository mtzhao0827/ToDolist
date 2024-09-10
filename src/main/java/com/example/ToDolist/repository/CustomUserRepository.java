package com.example.ToDolist.repository;

import com.example.ToDolist.exception.user.UserNotFoundException;
import com.example.ToDolist.model.User;
import org.springframework.data.repository.NoRepositoryBean;

public interface CustomUserRepository {
    User findByAuthenticatedUser(User authenticatedUser) throws UserNotFoundException;
}