package com.example.ToDolist.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ToDolist.model.ToDolist;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends JpaRepository<ToDolist, Long> {
}
