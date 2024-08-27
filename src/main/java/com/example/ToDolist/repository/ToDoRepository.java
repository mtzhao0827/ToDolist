package com.example.ToDolist.repository;
import com.example.ToDolist.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ToDolist.model.ToDo;

//import java.awt.print.Pageable;
import org.springframework.data.domain.Pageable;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Page<ToDo> findAll(Pageable pageable);
    Page<ToDo> findByUser(User user, Pageable pageable);
}
