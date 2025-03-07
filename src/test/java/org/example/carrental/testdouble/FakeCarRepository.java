package org.example.carrental.testdouble;

import org.example.carrental.car.domain.Car;
import org.example.carrental.car.service.port.CarRepository;

import java.util.*;
import java.util.stream.Collectors;

public class FakeCarRepository implements CarRepository {
    Map<Long, Car> cars = new HashMap<>();
    long nextId = 1L;

    @Override
    public Car saveCar(Car car) {
        if (car.getId() == null) {
            car = Car.builder().id(nextId++).name(car.getName()).brand(car.getBrand()).status(car.getStatus()).tags(car.getTags() != null ? new ArrayList<>(car.getTags()) : new ArrayList<>()).build();
        }
        cars.put(car.getId(), car);
        return car;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(cars.get(id));
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }

    @Override
    public List<Car> findAllByBrandContaining(String brand) {
        return cars.values().stream()
                .filter(car -> car.getBrand().contains(brand))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> findAllByNameContaining(String name) {
        return cars.values().stream()
                .filter(car -> car.getName().contains(name))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndBrand(String name, String brand) {
        return cars.values().stream()
                .anyMatch(car -> car.getName().equals(name) && car.getBrand().equals(brand));
    }


    @Override
    public void deleteById(Long id) {
        cars.remove(id);
    }


    public List<Car> findAllByTagId(Long tagId) {
        return cars.values().stream()
                .filter(car -> car.getTags().stream().anyMatch(tag -> tag.getId().equals(tagId)))
                .collect(Collectors.toList());
    }

}
