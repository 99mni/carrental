package org.example.carrental.car.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.carrental.car.controller.request.CarRequest;
import org.example.carrental.car.controller.request.TagRequest;
import org.example.carrental.car.controller.response.CarResponse;
import org.example.carrental.car.domain.Car;
import org.example.carrental.controller.processor.FileProcessor;
import org.example.carrental.car.service.CarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor //생성자 바로 처리
public class CarController {

    private final CarService carService;

    @PostMapping("/car")
    @Operation(summary = "자동차 등록")
    public ResponseEntity<?> registerCar(@Valid @RequestBody CarRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(carService.registerCar(request));
    }

    @GetMapping("/cars")
    @Operation(summary = "자동차 조회")
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/search/name")
    public List<CarResponse> searchCarsByModel(@RequestParam String keyword) {
        return carService.getCarsByNameContaining(keyword);
    }

    @GetMapping("/search/brand")
    public List<CarResponse> searchCarsByBrand(@RequestParam String keyword) {
        return carService.getCarsByBrandContaining(keyword);
    }

    @GetMapping("/car/{id}")
    @Operation(summary = "특정 자동차 렌탈 상태 확인")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @DeleteMapping("/car/{id}")
    @Operation(summary = "자동차 삭제")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/car/{id}")
    @Operation(summary = "자동차 수정")
    public ResponseEntity<?> updateCar(@PathVariable Long id,@Valid @RequestBody CarRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

    private final List<FileProcessor> fileProcessors;

    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "엑셀 배치 처리")
    public ResponseEntity<String> uploadCars(@RequestParam("file") MultipartFile file) {
        try {
            fileProcessors.forEach(processor -> System.out.println("등록된 프로세서: " + processor.getClass().getName()));

            List<Car> cars = fileProcessors.stream()
                    .filter(processor -> processor.supportsFileType(file))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 파일 형식입니다."))
                    .calculateFile(file);

            carService.saveCarsFrom(cars);
            return ResponseEntity.ok("배치 등록이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("배치 등록 실패: " + e.getMessage());
        }
    }

    @PostMapping("/tag")
    @Operation(summary = "태그 등록")
    public ResponseEntity<?> createTag(@RequestBody TagRequest request) {
        return ResponseEntity.ok(carService.registerTag(request));
    }
//    @PostMapping("/tag/{carId}")
//    @Operation(summary = "자동차 - 태그 연동")
//    public ResponseEntity<CarResponse> addTagToCar(@PathVariable Long carId, @RequestBody TagRequest tagRequest) {
//        CarResponse updatedCar = carService.addTagToCar(carId, tagRequest);
//        return ResponseEntity.ok(updatedCar);
//    }

    @DeleteMapping("/car/{carId}/tag/{tagName}")
    @Operation(summary = "태그 삭제")
    public ResponseEntity<Void> deleteTag(@PathVariable Long carId, @PathVariable String tagName) {
        carService.removeTag(carId, tagName);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/filter/{tagName}")
    @Operation(summary = "태그로 필터링")
    public ResponseEntity<List<CarResponse>> filterCarsByTag(@PathVariable String tagName) {
        return ResponseEntity.ok(carService.filterCarsByTag(tagName));
    }
}