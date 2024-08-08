package com.example.ToDolist.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ToDolist.model.ToDolist;

//import java.awt.print.Pageable;
import org.springframework.data.domain.Pageable;

public interface ToDoRepository extends JpaRepository<ToDolist, Long> {
    Page<ToDolist> findAll(Pageable pageable);
}
