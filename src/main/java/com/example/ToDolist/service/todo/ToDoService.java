package com.example.ToDolist.service.todo;

import com.example.ToDolist.model.ToDo;
import com.example.ToDolist.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ToDoService {
    Page<ToDo> getTodos(Pageable pageable, User user);
    ToDo createToDo(ToDo newtodo, User user);
    void deleteToDo(Long id, User authenticatedUser);
    ToDo updateToDo(Long id, ToDo updatedToDo, User authenticatedUser);
}
