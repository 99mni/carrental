package org.example.carrental.service;

import org.example.carrental.DTO.RentalRequestDTO;
import org.example.carrental.DTO.RentalResponseDTO;
import org.example.carrental.entity.Car;
import org.example.carrental.entity.Rental;
import org.example.carrental.entity.User;
import org.example.carrental.repository.CarRepository;
import org.example.carrental.repository.CarTagRepository;
import org.example.carrental.repository.RentalRepository;
import org.example.carrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("렌탈 서비스 테스트")
@SpringBootTest
public class RentalServiceTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    private Car car;
    private User user;
    private Rental rental;

    @Autowired
    private CarTagRepository carTagRepository;

    @BeforeEach
    void setUp() {
        carTagRepository.deleteAll();
        rentalRepository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();

        car = carRepository.save(Car.builder().name("자동차").brand("회사").status("available").build());
        user = userRepository.save(User.builder().name("사용자").build());
        rental = rentalService.rentCar(RentalRequestDTO.builder().carId(car.getId()).userId(user.getId())
                .rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(3))
                .status("rented").build());
    }

    @Test
    @DisplayName("렌탈 등록 테스트_성공")
    void rentCarTest_Success() {
        assertNotNull(rental);
        assertEquals("rented", rental.getStatus());
        assertEquals("rented", carRepository.findById(car.getId()).get().getStatus());
    }
    @Test
    @DisplayName("렌탈 등록 테스트_실패_자동차")
    void testRentCar_CarNotFound() {
        RentalRequestDTO invalidRental = RentalRequestDTO.builder().carId(999L).userId(user.getId())
                .rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(3)).build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.rentCar(invalidRental));
        assertEquals("자동차 ID 999 를 찾을 수 없음", exception.getMessage());
    }
    @Test
    @DisplayName("렌탈 등록 테스트_실패_사용자")
    void testRentCar_UserNotFound() {
        RentalRequestDTO invalidRental = RentalRequestDTO.builder().carId(car.getId()).userId(9999L)
                .rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(3)).build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.rentCar(invalidRental));
        assertEquals("사용자를 찾을 수 없음", exception.getMessage());
    }

    @Test
    @DisplayName("모든 자동차 렌탈 상태")
    void getAllActiveRentalsTest() {
        List<RentalResponseDTO> activeRentals = rentalService.getAllActiveRentals();

        assertEquals(1, activeRentals.size());
    }
    @Test
    @DisplayName("렌탈한 자동차 확인 테스트")
    void getRentalStatusTest() {
        String Status = rentalService.getRentalStatus(car.getId());
        assertEquals("rented", Status);
    }
    @Test
    @DisplayName("가능한 자동차 확인 테스트")
    void getRentalStatusTest_available() {
        rentalService.returnCarByCarId(car.getId());
        String Status = rentalService.getRentalStatus(car.getId());
        assertEquals("available", Status);
    }

    @Test
    @DisplayName("자동차 반납 테스트")
    void returnCarTest() {
        rentalService.returnCarByCarId(car.getId());

        Car returnedCar = carRepository.findById(car.getId()).orElseThrow();
        assertEquals("available", returnedCar.getStatus());

        Rental updatedRental = rentalRepository.findById(rental.getId()).orElseThrow();
        assertEquals("returned", updatedRental.getStatus());
    }
    @Test
    @DisplayName("자동차 반납 테스트_실패")
    void returnCarTest_CarNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> rentalService.returnCarByCarId(999L));
        assertEquals("렌탈 내역을 찾을 수 없음", exception.getMessage());
    }


}