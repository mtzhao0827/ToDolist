package com.example.ToDolist.exception.basic.notfound;

// 为什么要写extends RuntimeException？ 这样NotFoundException才是一个exception，不然它只是一个标准的class。
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message); // super是调用父类的构造函数
    }
}
