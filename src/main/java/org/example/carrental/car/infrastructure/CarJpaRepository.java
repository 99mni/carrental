package org.example.carrental.car.infrastructure;

import org.example.carrental.car.infrastructure.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarJpaRepository extends JpaRepository<CarEntity, Long> {
    List<CarEntity> findAllByBrandContaining(String brand);
    List<CarEntity> findAllByNameContaining(String name);
    boolean existsByNameAndBrand(String name, String brand);

}
