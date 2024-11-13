package com.example.ToDolist.exception.basic.internalservererror;

import com.example.ToDolist.exception.ExceptionResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InteralServerErrorAdvice {
    @ResponseBody
    @ExceptionHandler(InteralServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponseBody internalServerErrorAdvice(InteralServerErrorException ex) {
        return new ExceptionResponseBody(ex.getMessage());
    }
}
