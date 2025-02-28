package org.example.carrental.controller.processor;

import org.example.carrental.entity.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileProcessor {

    boolean supportsFileType(MultipartFile file);
    List<Car> calculateFile(MultipartFile file) throws Exception;
}
