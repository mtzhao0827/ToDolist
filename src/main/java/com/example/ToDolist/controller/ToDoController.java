package com.example.ToDolist.controller;

import com.example.ToDolist.model.ToDo;
import com.example.ToDolist.model.User;
import com.example.ToDolist.service.todo.ToDoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/v1/users/todos")
public class ToDoController {

    private final ToDoService todoService;

    // 分页
    @GetMapping("")
    public ResponseEntity<Page<ToDo>> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @AuthenticationPrincipal User authenticatedUser
        ){
        return ResponseEntity.status(HttpStatus.OK).body(todoService.getTodos(PageRequest.of(page, size),authenticatedUser));
    }

    // 创建ToDo
    @PostMapping("")
    public ResponseEntity<ToDo> createToDo(@RequestBody ToDo newtodo, @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createToDo(newtodo, authenticatedUser));
    }

    // 删除ToDo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id, @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(todoService.deleteToDo(id, authenticatedUser));
    }

    // 修改todo
    @PatchMapping("/{id}")
    public ResponseEntity<ToDo> updateToDo(
            @PathVariable Long id,
            @RequestBody ToDo updatedToDo,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(todoService.updateToDo(id, updatedToDo, authenticatedUser));
    }
}
