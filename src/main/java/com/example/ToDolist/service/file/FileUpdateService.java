package com.example.ToDolist.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileUpdateService {
    String uploadFile(MultipartFile file);
    byte[] getImage(String id);
}
