package org.example.carrental.car.service.port;

import org.example.carrental.car.domain.Car;
import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Car saveCar(Car car);
    Optional<Car> findById(Long id);
    List<Car> findAll();
    List<Car> findAllByBrandContaining(String brand);
    List<Car> findAllByNameContaining(String name);
    boolean existsByNameAndBrand(String name, String brand);
    void deleteById(Long id);

}