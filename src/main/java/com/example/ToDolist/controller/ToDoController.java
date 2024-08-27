package com.example.ToDolist.controller;

import com.example.ToDolist.exception.todo.TodoNotFoundException;
import com.example.ToDolist.exception.user.UserNotFoundException;
import com.example.ToDolist.model.ToDo;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.ToDoRepository;
import com.example.ToDolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/todos")
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

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

    // 分页
    @GetMapping("")
    public ResponseEntity<Page<ToDo>> getTodos(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
        ){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(()->new UserNotFoundException(userId));

        Pageable pageable = PageRequest.of(page, size);
        Page<ToDo> todos = toDoRepository.findByUser(user,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }

    // 创建ToDo
    @PostMapping("")
    public ResponseEntity<ToDo> createToDo(@PathVariable Long userId, @RequestBody ToDo newtodo) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        newtodo.setUser(user);
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
    public ResponseEntity<ToDo> updateToDo(@PathVariable Long id, @RequestBody ToDo updatedToDo) {
        Optional<ToDo> optionaltodo = toDoRepository.findById(id);
        if (optionaltodo.isPresent()){
            ToDo todo = optionaltodo.get();
            if (updatedToDo.getContent() != null) {
                todo.setContent(updatedToDo.getContent());
            }
            if (updatedToDo.isCompleted() != null) {
                todo.setCompleted(updatedToDo.isCompleted());
            }
            return ResponseEntity.status(HttpStatus.OK).body(toDoRepository.save(todo));
        }
        else{
            throw new TodoNotFoundException(id);
        }
    }
}
