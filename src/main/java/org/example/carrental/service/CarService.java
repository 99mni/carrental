package org.example.carrental.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.carrental.DTO.CarRequest;
import org.example.carrental.DTO.CarResponseDTO;
import org.example.carrental.DTO.TagRequest;
import org.example.carrental.DTO.TagResponseDTO;
import org.example.carrental.entity.Car;
import org.example.carrental.entity.CarTag;
import org.example.carrental.entity.Tag;
import org.example.carrental.repository.CarRepository;
import org.example.carrental.repository.CarTagRepository;
import org.example.carrental.repository.TagRepository;
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
    private final CarTagRepository carTagRepository;
    private final TagRepository tagRepository;

    public CarResponseDTO registerCar(CarRequest request) {
        Car car = Car.builder().name(request.getName()).brand(request.getBrand()).status(request.getStatus()).carTags(new ArrayList<>()).build();
        Car savedCar = carRepository.save(car);

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            request.getTagIds().forEach(tagId -> {
                Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Tag not found: " + tagId));
                CarTag carTag = CarTag.builder().car(savedCar).tag(tag).build();
                carTagRepository.save(carTag);
                savedCar.getCarTags().add(carTag);
            });
        }
        return CarResponseDTO.fromEntity(savedCar);
    }

    public List<CarResponseDTO> getAllCars() {
        return carRepository.findAll().stream().map(CarResponseDTO::fromEntity).collect(Collectors.toList());
    }

    public CarResponseDTO getCarById(Long id) {
        return carRepository.findById(id).map(CarResponseDTO::fromEntity).orElseThrow(() -> new RuntimeException("해당 ID의 자동차를 찾을 수 없음"));
    }

    public List<CarResponseDTO> getCarsByNameContaining(String name) {
        return carRepository.findAllByNameContaining(name).stream().map(CarResponseDTO::fromEntity).collect(Collectors.toList());
    }

    public List<CarResponseDTO> getCarsByBrandContaining(String brand) {
        return carRepository.findAllByBrandContaining(brand).stream().map(CarResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 자동차를 찾을 수 없음"));

        if ("rented".equals(car.getStatus())) {
            throw new RuntimeException("렌탈 중인 자동차는 삭제할 수 없음");
        }
        carRepository.deleteById(id);
        log.info("자동차(ID: {}) 삭제 완료", id);
    }

    // 동시성문제
    @Transactional
    public CarResponseDTO updateCar(Long id, CarRequest updatedRequest) {
        Car existingCar = carRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 자동차를 찾을 수 없음"));

        existingCar.update(updatedRequest.getName(), updatedRequest.getBrand(), updatedRequest.getStatus());

        carTagRepository.deleteByCarId(id);

        if (updatedRequest.getTagIds() != null && !updatedRequest.getTagIds().isEmpty()) {
            List<CarTag> updatedTags = updatedRequest.getTagIds().stream().map(tagId -> {
                Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Tag not found: " + tagId));
                return CarTag.builder().car(existingCar).tag(tag).build();
            }).collect(Collectors.toList());

            carTagRepository.saveAll(updatedTags);
            existingCar.getCarTags().clear();
            existingCar.getCarTags().addAll(updatedTags);
        }
        return CarResponseDTO.fromEntity(existingCar);
    }

    //배치처리
    @Transactional
    public void saveCarsFrom(List<Car> cars) {
        if (cars.isEmpty()) {
            throw new IllegalArgumentException("등록할 자동차 데이터가 없음");
        }
        carRepository.saveAll(cars);
    }

    //해시태그 추가
    @Transactional
    public TagResponseDTO registerTag(TagRequest request) {
        tagRepository.findByName(request.getName()).ifPresent(exisitingTag -> {
            throw new IllegalArgumentException("이미 존재하는 태그");
        });
        Tag tag = Tag.builder().name(request.getName()).build();
        return TagResponseDTO.fromEntity(tagRepository.save(tag));
    }
    @Transactional
    public CarResponseDTO addTagToCar(Long carId, TagRequest tagRequest) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("자동차를 찾을 수 없습니다: ID " + carId));

        Tag tag = tagRepository.findByName(tagRequest.getName()).orElseGet(() -> tagRepository.save(new Tag(tagRequest.getName())));
        
        CarTag carTag = CarTag.builder().car(car).tag(tag).build();
        carTagRepository.save(carTag);

        car.getCarTags().add(carTag);
        carRepository.save(car);

        return CarResponseDTO.fromEntity(car);
    }

    //    해시태그 제거
    @Transactional
    public void removeTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("Tag not found: " + tagId));
        carTagRepository.deleteByTagId(tagId);
        tagRepository.delete(tag);
    }
    //태그 필터링
    @Transactional
    public List<CarResponseDTO> filterCarsByTag(Long tagId) {
        return carTagRepository.findByTag_TagId(tagId).stream()
                .map(CarTag::getCar)
                .map(CarResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

}