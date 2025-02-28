package org.example.carrental.controller.processor;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.carrental.entity.Car;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
//@Deprecated
public class ExcelProcessor implements FileProcessor {

    @Override
    public boolean supportsFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null && fileName.endsWith(".xlsx");
    }

    @Override
    public List<Car> calculateFile(MultipartFile file) {
        List<Car> cars = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String name = row.getCell(0).getStringCellValue();
                String brand = row.getCell(1).getStringCellValue();
                String status = row.getCell(2).getStringCellValue();

                if (name == null || brand == null || status == null) {
                    System.out.println("데이터 누락된 행 스킵: Row " + row.getRowNum());
                    continue;
                }

                cars.add(Car.builder()
                        .name(name)
                        .brand(brand)
                        .status(status)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("⚡ Excel 파싱 중 오류 발생: " + e.getMessage());
        }
        return cars;
    }

}