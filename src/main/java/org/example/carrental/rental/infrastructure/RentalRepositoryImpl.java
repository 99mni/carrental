package org.example.carrental.rental.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.carrental.car.infrastructure.entity.CarEntity;
import org.example.carrental.rental.domain.Rental;
import org.example.carrental.rental.infrastructure.entity.RentalEntity;
import org.example.carrental.rental.service.port.RentalRepository;
import org.example.carrental.user.infrastructure.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.example.carrental.car.domain.Car;
import org.example.carrental.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RentalRepositoryImpl implements RentalRepository {

    private final RentalJpaRepository rentalJpaRepository;

    @Override
    public Rental save(Rental rental) {
        RentalEntity rentalEntity = RentalEntity.builder()
                .id(rental.getId())
                .car(CarEntity.builder()
                        .id(rental.getCar().getId())
                        .name(rental.getCar().getName())
                        .brand(rental.getCar().getBrand())
                        .status(rental.getCar().getStatus())
                        .build())
                .user(UserEntity.builder()
                        .id(rental.getUser().getId())
                        .name(rental.getUser().getName())
                        .build())
                .rentalDate(rental.getRentalDate())
                .returnDate(rental.getReturnDate())
                .status(rental.getStatus())
                .build();

        RentalEntity savedEntity = rentalJpaRepository.save(rentalEntity);

        return new Rental(
                savedEntity.getId(),
                rental.getCar(),
                rental.getUser(),
                savedEntity.getRentalDate(),
                savedEntity.getReturnDate(),
                savedEntity.getStatus()
        );
    }

    @Override
    public List<Rental> findAllByStatus(String status) {
        return rentalJpaRepository.findAllByStatus(status).stream()
                .map(entity -> new Rental(
                        entity.getId(),
                        new Car(entity.getCar().getId(), entity.getCar().getName(), entity.getCar().getBrand(), entity.getCar().getStatus()),
                        new User(entity.getUser().getId(), entity.getUser().getName()),
                        entity.getRentalDate(),
                        entity.getReturnDate(),
                        entity.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Rental> findActiveRentalByCarId(Long carId) {
        return rentalJpaRepository.findByCarIdAndStatus(carId, "rented")
                .map(entity -> new Rental(
                        entity.getId(),
                        new Car(entity.getCar().getId(), entity.getCar().getName(), entity.getCar().getBrand(), entity.getCar().getStatus()),
                        new User(entity.getUser().getId(), entity.getUser().getName()),
                        entity.getRentalDate(),
                        entity.getReturnDate(),
                        entity.getStatus()
                ));
    }
}
