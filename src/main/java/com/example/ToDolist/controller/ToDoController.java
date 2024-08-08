package com.example.ToDolist.controller;

import com.example.ToDolist.model.ToDolist;
import com.example.ToDolist.repository.ToDoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/todos")
@Controller
@ResponseBody
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepository;
    private List<ToDolist> todos;
    //private AtomicLong counter;
//    public ToDoController() {
//        todos = new ArrayList<>();
//        todos.add(new ToDolist("Learn Spring Boot", false));
//        todos.add(new ToDolist("Build a todo list", false));
//        todos.add(new ToDolist("Write a blog post", true));
//        todos.add(new ToDolist("Attend Spring Boot meetup", false));
//    }
//
//    @PostConstruct
//    public void init() {
//        for (ToDolist todo : todos) {
//            toDoRepository.save(todo);
//        }
//    }

    @GetMapping("")
    public ResponseEntity<List<ToDolist>> getTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(toDoRepository.findAll());
    }

    // 创建ToDo
    @PostMapping("")
    public ResponseEntity<ToDolist> createToDo(@RequestBody ToDolist newtodo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toDoRepository.save(newtodo));
    }

    // 删除ToDo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id) {
        toDoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // 修改todo
    @PatchMapping("/{id}")
    public ResponseEntity<ToDolist> updateToDo(@PathVariable Long id, @RequestBody ToDolist updatedToDo) {
        Optional<ToDolist> optionaltodo = toDoRepository.findById(id);
        if (optionaltodo.isPresent()){
            ToDolist todo = optionaltodo.get();
            if (updatedToDo.getContent() != null) {
                todo.setContent(updatedToDo.getContent());
            }
            if (updatedToDo.isCompleted() != null) {
                todo.setCompleted(updatedToDo.isCompleted());
            }
            return ResponseEntity.status(HttpStatus.OK).body(toDoRepository.save(todo));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
