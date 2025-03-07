package org.example.carrental.testdouble;

import org.example.carrental.rental.domain.Rental;
import org.example.carrental.rental.service.port.RentalRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeRentalRepository implements RentalRepository {

    Map<Long, Rental> rentals = new HashMap<>();
    Long nextId = 1L;

    @Override
    public Rental save(Rental rental) {
        if (rental.getId() == null) {
            rental = Rental.builder().id(nextId++).car(rental.getCar()).user(rental.getUser())
                    .rentalDate(rental.getRentalDate()).returnDate(rental.getReturnDate()).status(rental.getStatus()).build();
        }
        rentals.put(rental.getId(), rental);
        return rental;
    }

    @Override
    public List<Rental> findAllByStatus(String status) {
        return rentals.values().stream()
                .filter(rental -> rental.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Rental> findActiveRentalByCarId(Long carId) {
        return rentals.values().stream()
                .filter(rental -> rental.getCar().getId().equals(carId) && "rented".equals(rental.getStatus()))
                .findFirst();
    }

    public Optional<Rental> findById(Long id) {
        return Optional.ofNullable(rentals.get(id));
    }

    public void deleteById(Long id) {
        rentals.remove(id);
    }
}
