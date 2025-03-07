package org.example.carrental.car.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.carrental.car.domain.Car;
import org.example.carrental.car.domain.Tag;
import org.example.carrental.car.infrastructure.entity.CarEntity;
import org.example.carrental.car.infrastructure.entity.TagEntity;
import org.example.carrental.car.service.port.CarRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepository {

    private final CarJpaRepository carJpaRepository;
    private final TagJpaRepository tagJpaRepository;

    @Transactional
    @Override
    public Car saveCar(Car car){
            CarEntity carEntity = CarEntity.builder().name(car.getName()).brand(car.getBrand()).status(car.getStatus()).build();

            if (car.getTags() != null && !car.getTags().isEmpty()) {
                List<TagEntity> tagEntities = car.getTags().stream()
                        .map(tag -> tagJpaRepository.findById(tag.getId()).orElseThrow(() -> new RuntimeException("Tag not found with id: " + tag.getId()))).collect(Collectors.toList());
                carEntity = CarEntity.builder().id(carEntity.getId()).name(carEntity.getName()).brand(carEntity.getBrand()).status(carEntity.getStatus()).tags(tagEntities).build();
            }

            CarEntity savedEntity = carJpaRepository.save(carEntity);

            return Car.builder()
                .id(savedEntity.getId())
                .name(savedEntity.getName())
                .brand(savedEntity.getBrand())
                .status(savedEntity.getStatus())
                .tags(savedEntity.getTags().stream()
                        .map(tagEntity -> Tag.builder().id(tagEntity.getId()).name(tagEntity.getName()).build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Optional<Car> findById(Long id) {
        return carJpaRepository.findById(id).map(entity -> Car.builder()
                .id(entity.getId())
                .name(entity.getName())
                .brand(entity.getBrand())
                .status(entity.getStatus())
                .tags(entity.getTags().stream()
                        .map(tagEntity -> Tag.builder().id(tagEntity.getId()).name(tagEntity.getName()).build())
                        .collect(Collectors.toList()))
                .build());
    }



    @Override
    public List<Car> findAll() {
        return carJpaRepository.findAll().stream()
                .map(entity -> Car.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .brand(entity.getBrand())
                        .status(entity.getStatus())
                        .tags(entity.getTags().stream()
                                .map(tagEntity -> Tag.builder().id(tagEntity.getId()).name(tagEntity.getName()).build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public List<Car> findAllByNameContaining(String name) {
        return carJpaRepository.findAllByNameContaining(name).stream()
                .map(entity -> Car.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .brand(entity.getBrand())
                        .status(entity.getStatus())
                        .tags(entity.getTags().stream()
                                .map(tagEntity -> Tag.builder().id(tagEntity.getId()).name(tagEntity.getName()).build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> findAllByBrandContaining(String brand) {
        return carJpaRepository.findAllByBrandContaining(brand).stream()
                .map(entity -> Car.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .brand(entity.getBrand())
                        .status(entity.getStatus())
                        .tags(entity.getTags().stream()
                                .map(tagEntity -> Tag.builder().id(tagEntity.getId()).name(tagEntity.getName()).build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndBrand(String name, String brand) {
        return carJpaRepository.existsByNameAndBrand(name, brand);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        carJpaRepository.deleteById(id);
    }

}
