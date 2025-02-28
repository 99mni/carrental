package org.example.carrental.repository;

import org.example.carrental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> name(String name);

    List<Car> findAllByNameContaining(String name);
    List<Car> findAllByBrandContaining(String brand);

}