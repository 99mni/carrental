package org.example.carrental.car.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carrental.car.controller.request.CarRequest;
import org.example.carrental.car.controller.response.CarResponse;
import org.example.carrental.car.controller.request.TagRequest;
import org.example.carrental.car.controller.response.TagResponse;
import org.example.carrental.car.domain.Car;
import org.example.carrental.car.domain.Tag;
import org.example.carrental.car.infrastructure.entity.TagEntity;
import org.example.carrental.car.service.port.CarRepository;
import org.example.carrental.car.service.port.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {

    private final CarRepository carRepository;
    private final TagRepository tagRepository;

    @Transactional
    public CarResponse registerCar(CarRequest request) {
        List<Tag> tags = new ArrayList<>();

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            tags = request.getTagNames().stream()
                    .map(tagName -> tagRepository.findByName(tagName)
                            .orElseGet(() -> tagRepository.saveTag(Tag.builder().name(tagName).build()))
                    ).collect(Collectors.toList());
        }

        Car car = Car.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .status(request.getStatus())
                .tags(tags)
                .build();

        Car savedCar = carRepository.saveCar(car);
        return new CarResponse(savedCar.getId(), savedCar.getName(), savedCar.getBrand(), savedCar.getStatus(),
                savedCar.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
    }

    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> new CarResponse(car.getId(), car.getName(), car.getBrand(), car.getStatus(),
                        car.getTags().stream().map(Tag::getName).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 자동차를 찾을 수 없음"));
        return new CarResponse(car.getId(), car.getName(), car.getBrand(), car.getStatus(),
                car.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
    }

    public List<CarResponse> getCarsByNameContaining(String name) {
        return carRepository.findAllByNameContaining(name).stream()
                .map(car -> new CarResponse(car.getId(), car.getName(), car.getBrand(), car.getStatus(),
                        car.getTags().stream().map(Tag::getName).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public List<CarResponse> getCarsByBrandContaining(String brand) {
        return carRepository.findAllByBrandContaining(brand).stream()
                .map(car -> new CarResponse(car.getId(), car.getName(), car.getBrand(), car.getStatus(),
                        car.getTags().stream().map(Tag::getName).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 자동차를 찾을 수 없음"));

        if ("rented".equals(car.getStatus())) {
            throw new RuntimeException("렌탈 중인 자동차는 삭제할 수 없음");
        }

        carRepository.deleteById(id);
        log.info("자동차(ID: {}) 삭제 완료", id);
    }


    // 동시성문제
    @Transactional
    public CarResponse updateCar(Long id, CarRequest updatedRequest) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 자동차를 찾을 수 없음"));

        existingCar.update(updatedRequest.getName(), updatedRequest.getBrand(), updatedRequest.getStatus());

        List<Tag> updatedTags = new ArrayList<>();
        if (updatedRequest.getTagNames() != null && !updatedRequest.getTagNames().isEmpty()) {
            updatedTags = updatedRequest.getTagNames().stream()
                    .map(tagName -> tagRepository.findByName(tagName)
                            .orElseGet(() -> tagRepository.saveTag(Tag.builder().name(tagName).build())))
                    .collect(Collectors.toList());
        }

        Car updatedCar = Car.builder()
                .id(existingCar.getId())
                .name(existingCar.getName())
                .brand(existingCar.getBrand())
                .status(existingCar.getStatus())
                .tags(updatedTags)
                .build();

        carRepository.saveCar(updatedCar);
        return new CarResponse(updatedCar.getId(), updatedCar.getName(), updatedCar.getBrand(), updatedCar.getStatus(),
                updatedCar.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
    }

    //배치처리
    @Transactional
    public void saveCarsFrom(List<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            throw new IllegalArgumentException("등록할 자동차 데이터가 없음");
        }

        for (Car car : cars) {
            boolean exists = carRepository.existsByNameAndBrand(car.getName(), car.getBrand());
            if (!exists) {
                carRepository.saveCar(car);
            } else {
                System.out.println("중복 데이터 발견: " + car.getName() + " - " + car.getBrand());
            }
        }
    }


    //해시태그 추가
    @Transactional
    public TagResponse registerTag(TagRequest request) {
        tagRepository.findByName(request.getName()).ifPresent(existingTag -> {
            throw new IllegalArgumentException("이미 존재하는 태그");
        });
        Tag tag = Tag.builder().name(request.getName()).build();
        return new TagResponse(tagRepository.saveTag(tag));
    }


    //    해시태그 제거
    @Transactional
    public void removeTag(Long carId, String tagName) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("자동차를 찾을 수 없습니다: ID " + carId));

        List<Tag> updatedTags = car.getTags().stream()
                .filter(tag -> !tag.getName().equals(tagName))
                .collect(Collectors.toList());

        Car updatedCar = Car.builder()
                .id(car.getId())
                .name(car.getName())
                .brand(car.getBrand())
                .status(car.getStatus())
                .tags(updatedTags)
                .build();

        carRepository.saveCar(updatedCar);
    }

    @Transactional
    public List<CarResponse> filterCarsByTag(String tagName) {
        return carRepository.findAll().stream()
                .filter(car -> car.getTags().stream().anyMatch(tag -> tag.getName().equals(tagName)))
                .map(car -> new CarResponse(car.getId(), car.getName(), car.getBrand(), car.getStatus(),
                        car.getTags().stream().map(Tag::getName).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

//    @Transactional
//    public CarResponse addTagToCar(Long carId, TagRequest tagRequest) {
//        Car car = carRepository.findById(carId)
//                .orElseThrow(() -> new IllegalArgumentException("자동차를 찾을 수 없습니다: ID " + carId));
//
//        Tag tag = tagRepository.findByName(tagRequest.getName())
//                .orElseGet(() -> tagRepository.saveTag(Tag.builder().name(tagRequest.getName()).build()));
//
//        List<Tag> updatedTags = new ArrayList<>(car.getTags());
//        updatedTags.add(tag);
//
//        Car updatedCar = Car.builder().id(car.getId()).name(car.getName()).brand(car.getBrand()).status(car.getStatus()).tags(updatedTags).build();
//
//        carRepository.saveCar(updatedCar);
//
//        return new CarResponse(updatedCar.getId(), updatedCar.getName(), updatedCar.getBrand(), updatedCar.getStatus(),
//                updatedCar.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
//    }
}