package org.example.carrental.rental.infrastructure;

import org.example.carrental.rental.infrastructure.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalJpaRepository extends JpaRepository<RentalEntity, Long> {
    List<RentalEntity> findAllByStatus(String status);
    Optional<RentalEntity> findByCarIdAndStatus(Long carId, String status);
}
