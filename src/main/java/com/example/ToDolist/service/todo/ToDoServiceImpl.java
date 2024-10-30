package com.example.ToDolist.service.todo;

import com.example.ToDolist.exception.todo.TodoNotFoundException;
import com.example.ToDolist.exception.user.UserForbiddenException;
import com.example.ToDolist.exception.user.UserNotFoundException;
import com.example.ToDolist.model.ToDo;
import com.example.ToDolist.model.User;
import com.example.ToDolist.repository.ToDoRepository;
import com.example.ToDolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ToDoServiceImpl implements ToDoService{

    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;
    @Override
    public Page<ToDo> getTodos(Pageable pageable,User authenticatedUser) {
        Long userId = authenticatedUser.getId();
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(()->new UserNotFoundException(userId));
        Page<ToDo> todos = toDoRepository.findByUser(user,pageable);
        return todos;
    }

    @Override
    public ToDo createToDo(ToDo newtodo, User authenticatedUser) {
        Long userId = authenticatedUser.getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        newtodo.setUser(user);
        return toDoRepository.save(newtodo);
    }

    @Override
    public Void deleteToDo(Long id, User authenticatedUser) {
        ToDo todo = toDoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        if (!todo.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UserForbiddenException();
        }
        toDoRepository.deleteById(id);
        return null;
    }

    @Override
    public ToDo updateToDo(Long id, ToDo updatedToDo, User authenticatedUser) {
        ToDo todo = toDoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
        if (!todo.getUser().getId().equals(authenticatedUser.getId())){
            throw new UserForbiddenException();
        }
        if (updatedToDo.getContent() != null) {
            todo.setContent(updatedToDo.getContent());
        }

        if (updatedToDo.isCompleted() != null) {
            todo.setCompleted(updatedToDo.isCompleted());
        }
        return toDoRepository.save(todo);
    }

}
