package org.example.carrental.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carrental.DTO.RentalRequestDTO;
import org.example.carrental.DTO.RentalResponseDTO;
import org.example.carrental.common.controller.MyExceptionController;
import org.example.carrental.entity.Car;
import org.example.carrental.entity.Rental;
import org.example.carrental.entity.User;
import org.example.carrental.repository.CarRepository;
import org.example.carrental.repository.RentalRepository;
import org.example.carrental.repository.UserRepository;
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
    public Rental rentCar(RentalRequestDTO rental){

        User user = userRepository.findById(rental.getUserId()).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

        Car car = carRepository.findById(rental.getCarId()).orElseThrow(() -> new CanNotFoundException("자동차 ID " + rental.getCarId() + " 를 찾을 수 없음"));

        if("rented".equals(car.getStatus())){
            throw new CarAlreadyRentedException(car.getId());
        }

        Car updatedCar = car.toBuilder().status(rental.getStatus()).build();
        carRepository.save(updatedCar);

        Rental updatedRental = Rental.builder().car(updatedCar).user(user).rentalDate(rental.getRentalDate()).returnDate(rental.getReturnDate()).status(rental.getStatus()).build();

        return rentalRepository.save(updatedRental);
    }

    //자동차 렌탈 상태
    public List<RentalResponseDTO> getAllActiveRentals() {
        return rentalRepository.findAllByStatus("rented").stream()
                .map(RentalResponseDTO::new)
                .collect(Collectors.toList());
    }
    public String getRentalStatus(Long carId){
        Car car = carRepository.findById(carId)
                .orElseThrow(()->new CanNotFoundException("해당 자동차를 찾을 수 없음"));

        if ("rented".equals(car.getStatus())){
            return "rented";
        }
        return "available";
    }

    @Transactional
    public void returnCarByCarId(Long carId) {
        Rental rental = rentalRepository.findActiveRentalByCarId(carId)
                .orElseThrow(()-> new RuntimeException("렌탈 내역을 찾을 수 없음"));


        Car updatedCar = rental.getCar().toBuilder()
                        .status("available").build();
        carRepository.save(updatedCar);

        Rental updatedRental = rental.updateStatus("returned");
        rentalRepository.save(updatedRental);
    }


}
