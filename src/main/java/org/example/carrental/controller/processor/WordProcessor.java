package org.example.carrental.controller.processor;

import org.example.carrental.car.domain.Car;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public abstract class WordProcessor implements FileProcessor {
    @Override
    public boolean supportsFileType(MultipartFile file) {
        return file.getContentType().equals("word");
    }

    @Override
    public List<Car> calculateFile(MultipartFile file) {
        return null;
    }
}

