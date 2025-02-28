package org.example.carrental.repository;

import org.example.carrental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.car.id = :carId AND r.status = 'rented'")
    Optional<Rental> findActiveRentalByCarId(@Param("carId") Long carId);
    List<Rental> findAllByStatus(String status);
}