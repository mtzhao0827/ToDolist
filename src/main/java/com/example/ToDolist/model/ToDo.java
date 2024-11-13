package com.example.ToDolist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity // 标注该类为实体类，并且将其映射到数据库表
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 指定主键的生成方式，GenerationType.IDENTITY为自增
    private Long id;

    @NotBlank(message = "Content must not be blank!")
    private String content;
    private Boolean completed;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public ToDo() {}

    public ToDo(String content, Boolean completed, String filePath, User user) {
        this.content = content;
        this.completed = completed;
        this.user = user;
        this.filePath = filePath;
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

    public String getFilePath() { return filePath; }

    public void setFilePath(String filePath) { this.filePath = filePath; }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
