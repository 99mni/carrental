package org.example.carrental.rental.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.carrental.rental.controller.reqeust.RentalRequest;
import org.example.carrental.rental.controller.response.RentalResponse;
import org.example.carrental.rental.domain.Rental;
import org.example.carrental.rental.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/rental")
    @Operation(summary = "렌탈 등록")
    public ResponseEntity<?> registerRental(@Valid @RequestBody RentalRequest rentalRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Rental rental = rentalService.rentCar(rentalRequest);
        RentalResponse response = new RentalResponse(rental);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rentals")
    @Operation(summary = "렌탈 상태 전체 확인")
    public ResponseEntity<List<RentalResponse>> getAllActiveRentals() {
        return ResponseEntity.ok(rentalService.getAllActiveRentals());
    }


    @GetMapping("/rental/{carId}")
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
