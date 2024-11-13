package com.example.ToDolist.exception.file;

import com.example.ToDolist.exception.basic.internalservererror.InteralServerErrorException;

public class FileInteralServerErrorException extends InteralServerErrorException {
    public FileInteralServerErrorException () { super("Error uploading image!");}
}
