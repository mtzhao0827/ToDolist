package com.example.ToDolist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // 标注该类为实体类，并且将其映射到数据库表
public class ToDolist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //指定主键的生成方式，GenerationType.IDENTITY为自增
    private Long id;
    private String content;
    private Boolean completed;

    public ToDolist() {}

    public ToDolist(String content, Boolean completed) {
        this.content = content;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
