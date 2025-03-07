package org.example.carrental.rental.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carrental.car.domain.Car;
import org.example.carrental.rental.controller.reqeust.RentalRequest;
import org.example.carrental.rental.controller.response.RentalResponse;
import org.example.carrental.rental.domain.Rental;
import org.example.carrental.user.domain.User;
import org.example.carrental.car.service.port.CarRepository;
import org.example.carrental.rental.service.port.RentalRepository;
import org.example.carrental.user.service.port.UserRepository;
import org.springframework.stereotype.Service;
import org.example.carrental.common.exception.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    //렌탈 등록
    @Transactional
    public Rental rentCar(RentalRequest rental){

        User user = userRepository.findById(rental.getUserId()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));
        Car car = carRepository.findById(rental.getCarId()).orElseThrow(() -> new CanNotFoundException("자동차 ID " + rental.getCarId() + " 를 찾을 수 없음"));

        if("rented".equals(car.getStatus())){
            throw new CarAlreadyRentedException(car.getId());
        }

        Car updatedCar = Car.builder().id(car.getId()).name(car.getName()).brand(car.getBrand()).status(rental.getStatus()).build();
        carRepository.saveCar(updatedCar);

        Rental updatedRental = Rental.builder().car(updatedCar).user(user).rentalDate(rental.getRentalDate()).returnDate(rental.getReturnDate()).status(rental.getStatus()).build();

        return rentalRepository.save(updatedRental);
    }

    //자동차 렌탈 상태
    public List<RentalResponse> getAllActiveRentals() {
        return rentalRepository.findAllByStatus("rented").stream()
                .map(RentalResponse::new)
                .collect(Collectors.toList());
    }
    public String getRentalStatus(Long carId){
        Car car = carRepository.findById(carId)
                .orElseThrow(()->new CanNotFoundException("해당 자동차를 찾을 수 없음"));
        return "rented".equals(car.getStatus()) ? "rented" : "available";
    }

    @Transactional
    public void returnCarByCarId(Long carId) {
        Rental rental = rentalRepository.findActiveRentalByCarId(carId)
                .orElseThrow(()-> new RuntimeException("렌탈 내역을 찾을 수 없음"));


        Car updatedCar =  Car.builder()
                .id(rental.getCar().getId())
                .name(rental.getCar().getName())
                .brand(rental.getCar().getBrand())
                .status("available")
                .build();
        carRepository.saveCar(updatedCar);

        Rental updatedRental = Rental.builder()
                .id(rental.getId())
                .car(updatedCar)
                .user(rental.getUser())
                .rentalDate(rental.getRentalDate())
                .returnDate(rental.getReturnDate())
                .status("returned")
                .build();
        rentalRepository.save(updatedRental);
    }


}
