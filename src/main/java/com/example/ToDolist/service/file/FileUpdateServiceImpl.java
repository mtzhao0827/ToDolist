package com.example.ToDolist.service.file;

import com.example.ToDolist.exception.file.FileInteralServerErrorException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
@Service

public class FileUpdateServiceImpl implements  FileUpdateService{
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            // 保存上传的图片
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 返回文件路径
            return "/v1/files/"+fileName;
        } catch (IOException e) {
            throw new FileInteralServerErrorException();
        }
    }

    @Override
    public byte[] getImage(String id) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            String fileName =id;
            Path filePath = uploadPath.resolve(fileName);
            FileInputStream inputStream = new FileInputStream(filePath.toString());
            return inputStream.readAllBytes();

        } catch (IOException e) {
            throw new FileInteralServerErrorException();
        }
    }
}
