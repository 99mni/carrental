package org.example.carrental.rental.service.port;

import org.example.carrental.rental.domain.Rental;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository{

    Rental save(Rental rental);
    List<Rental> findAllByStatus(String status);
    Optional<Rental> findActiveRentalByCarId(Long carId); //잘못 쓸모없었다.

}