package com.example.ToDolist.controller;

import com.example.ToDolist.exception.todo.TodoNotFoundException;
import com.example.ToDolist.exception.user.UserForbiddenException;
import com.example.ToDolist.exception.user.UserNotFoundException;
import com.example.ToDolist.model.ToDo;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.ToDoRepository;
import com.example.ToDolist.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/users/todos")
public class ToDoController {

    private ToDoRepository toDoRepository;
    private UserRepository userRepository;

    // 分页
    @GetMapping("")
    public ResponseEntity<Page<ToDo>> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @AuthenticationPrincipal User authenticatedUser
        ){
        Long userId = authenticatedUser.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(()->new UserNotFoundException(userId));

        Pageable pageable = PageRequest.of(page, size);
        Page<ToDo> todos = toDoRepository.findByUser(user,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }

    // 创建ToDo
    @PostMapping("")
    public ResponseEntity<ToDo> createToDo(@RequestBody ToDo newtodo, @AuthenticationPrincipal User authenticatedUser) {
        Long userId = authenticatedUser.getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        newtodo.setUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDoRepository.save(newtodo));
    }

    // 删除ToDo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        Optional<ToDo> optionaltodo = toDoRepository.findById(id);
        if (!optionaltodo.isPresent()) {
            throw new TodoNotFoundException(id);
        }
        Long userId = authenticatedUser.getId();
        ToDo todo = optionaltodo.get();
        if (!todo.getUser().getId().equals(userId)){
            throw new UserForbiddenException();
        }
        toDoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // 修改todo
    @PatchMapping("/{id}")
    public ResponseEntity<ToDo> updateToDo(
            @PathVariable Long id,
            @RequestBody ToDo updatedToDo,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        Optional<ToDo> optionaltodo = toDoRepository.findById(id);
        if (!optionaltodo.isPresent()) {
            throw new TodoNotFoundException(id);
        }
        Long userId = authenticatedUser.getId();
        ToDo todo = optionaltodo.get();
        if (!todo.getUser().getId().equals(userId)){
            throw new UserForbiddenException();
        }
        if (updatedToDo.getContent() != null) {
                todo.setContent(updatedToDo.getContent());
        }
        if (updatedToDo.isCompleted() != null) {
                todo.setCompleted(updatedToDo.isCompleted());
        }
        return ResponseEntity.status(HttpStatus.OK).body(toDoRepository.save(todo));
    }
}
