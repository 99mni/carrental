package org.example.carrental.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.carrental.DTO.RentalRequestDTO;
import org.example.carrental.DTO.RentalResponseDTO;
import org.example.carrental.entity.Rental;
import org.example.carrental.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/rent")
    @Operation(summary = "렌탈 등록")
    public ResponseEntity<?> registerRental(@Valid @RequestBody RentalRequestDTO rentalRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Rental rental = rentalService.rentCar(rentalRequestDTO);
        RentalResponseDTO responseDTO = new RentalResponseDTO(rental);

        return ResponseEntity.ok(responseDTO);
    }

        @GetMapping("/all")
    @Operation(summary = "렌탈 상태 전체 확인")
    public ResponseEntity<List<RentalResponseDTO>> getAllActiveRentals() {
        return ResponseEntity.ok(rentalService.getAllActiveRentals());
    }


    @GetMapping("/status/{carId}")
    @Operation(summary = "특정 자동차 상태 확인")
    public ResponseEntity<Map<String, String>> getRentalStatus(@PathVariable Long carId) {
        String status = rentalService.getRentalStatus(carId);
        return ResponseEntity.ok(Map.of("status", status));
    }


    //carId로 반납
    @PutMapping("/return/{carId}")
    @Operation(summary = "자동차 반납")
    public ResponseEntity<String> returnCarByCarId(@PathVariable Long carId) {
        rentalService.returnCarByCarId(carId);
        return ResponseEntity.ok("자동차(ID: " + carId + ") 반납 완료");
    }

}
