package org.example.carrental.service;

import jakarta.transaction.Transactional;
import org.example.carrental.DTO.CarRequest;
import org.example.carrental.DTO.CarResponseDTO;
import org.example.carrental.DTO.TagRequest;
import org.example.carrental.DTO.TagResponseDTO;
import org.example.carrental.entity.Car;
import org.example.carrental.entity.Tag;
import org.example.carrental.repository.CarRepository;
import org.example.carrental.repository.CarTagRepository;
import org.example.carrental.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("자동차 서비스 테스트")
@SpringBootTest
@Transactional //테스트가 끝나면 강제로 롤백
public class CarServiceTest {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarTagRepository carTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        carTagRepository.deleteAll();
        tagRepository.deleteAll();
        carRepository.deleteAll();

    }

    @Test
    @DisplayName("자동차 등록 테스트")
    void registerCarTest() {
        CarRequest request = CarRequest.builder().name("자동차_테스트").brand("회사_테스트").build();
        CarResponseDTO savedCar = carService.registerCar(request);

        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getName()).isEqualTo("자동차_테스트");
        assertThat(savedCar.getBrand()).isEqualTo("회사_테스트");
        assertThat(savedCar.getStatus()).isEqualTo("available");
    }
    @Test
    @DisplayName("자동차 등록 테스트_태그 있음")
    void registerCarTagTest() {
        TagResponseDTO tagResponse = carService.registerTag(new TagRequest("7인승"));
        CarRequest request = CarRequest.builder().name("자동차_태그_테스트").brand("회사_테스트").tagIds(List.of(tagResponse.getTagId())).build();
        CarResponseDTO savedCar = carService.registerCar(request);

        assertThat(savedCar.getName()).isEqualTo("자동차_태그_테스트");
        assertThat(savedCar.getTags()).contains("7인승");
    }

    @Test
    @DisplayName("모든 자동차 조회 테스트")
    void getAllCarsTest() {
        carService.registerCar(CarRequest.builder().name("자동차1").brand("회사1").status("available").build());
        carService.registerCar(CarRequest.builder().name("자동차2").brand("회사2").status("available").build());

        List<CarResponseDTO> cars = carService.getAllCars();

        assertThat(cars).hasSize(2);
        assertThat(cars).extracting("name").containsExactlyInAnyOrder("자동차1", "자동차2");
    }

    @Test
    @DisplayName("ID 자동차 조회")
    void getCarByIdTest() {
        CarResponseDTO savedCar = carService.registerCar(CarRequest.builder().name("자동차").brand("회사").status("available").build());

        CarResponseDTO foundCar = carService.getCarById(savedCar.getId());

        assertThat(foundCar).isNotNull();
        assertThat(foundCar.getName()).isEqualTo("자동차");
    }

    @Test
    @DisplayName("자동차 이름으로 검색")
    void getCarsByNameTest() {
        carService.registerCar(CarRequest.builder().name("자동차1").brand("회사1").status("available").build());
        carService.registerCar(CarRequest.builder().name("자동차2").brand("회사2").status("available").build());

        List<CarResponseDTO> cars = carService.getCarsByNameContaining("자동차");

        assertThat(cars).hasSize(2);
    }
    @Test
    @DisplayName("회사명으로 검색")
    void getCarsByBrandTest() {
        carService.registerCar(CarRequest.builder().name("자동차1").brand("회사1").status("available").build());
        carService.registerCar(CarRequest.builder().name("자동차2").brand("회사2").status("available").build());

        List<CarResponseDTO> cars = carService.getCarsByBrandContaining("회사1");

        assertThat(cars).hasSize(1);
        assertThat(cars.get(0).getBrand()).isEqualTo("회사1");
    }

    @Test
    @DisplayName("자동차 삭제 테스트")
    void deleteCarTest() {
        CarResponseDTO savedCar = carService.registerCar(CarRequest.builder().name("자동차").brand("회사").status("available").build());

        carService.deleteCar(savedCar.getId());

        assertThat(carRepository.findById(savedCar.getId())).isEmpty();
    }
    @Test
    @DisplayName("자동차 삭제 실패 테스트")
    void deleteRentedCarTest() {
        CarRequest carRequest = CarRequest.builder().name("자동차").brand("회사").status("rented").build();
        CarResponseDTO savedCar = carService.registerCar(carRequest);

        assertThatThrownBy(() -> carService.deleteCar(savedCar.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("렌탈 중인 자동차는 삭제할 수 없음");
    }

    @Test
    @DisplayName("자동차 수정 테스트")
    void updateCarTest() {
        CarResponseDTO savedcar = carService.registerCar(CarRequest.builder().name("자동차_기존").brand("회사_기존").status("available").build());
        TagResponseDTO newTag = carService.registerTag(new TagRequest("스포츠"));
        CarRequest updateRequest = CarRequest.builder().name("자동차_수정").brand("회사_수정").status("rented").tagIds(List.of(newTag.getTagId())).build();

        CarResponseDTO updatedCar = carService.updateCar(savedcar.getId(), updateRequest);

        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar.getName()).isEqualTo("자동차_수정");
        assertThat(updatedCar.getBrand()).isEqualTo("회사_수정");
        assertThat(updatedCar.getStatus()).isEqualTo("rented");
        assertThat(updatedCar.getTags()).contains("스포츠");
    }

    @Test
    @DisplayName("자동차 배치")
    void saveCarFromTest() {
        List<Car> cars = List.of(
                Car.builder().name("자동차1").brand("회사1").status("available").build(),
                Car.builder().name("자동차2").brand("회사2").status("available").build()
        );

        carService.saveCarsFrom(cars);

        List<CarResponseDTO> allCars = carService.getAllCars();
        assertThat(allCars).hasSize(2);
        assertThat(allCars).extracting("name").containsExactlyInAnyOrder("자동차1", "자동차2");
    }
    @Test
    @DisplayName("빈 파일")
    void saveCarsFromEmptyListTest() {
        List<Car> emptyList = List.of();

        assertThatThrownBy(() -> carService.saveCarsFrom(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록할 자동차 데이터가 없음");
    }

    @Test
    @DisplayName("자동차 태그 추가 테스트")
    void registerTag() {
        TagRequest request = TagRequest.builder().name("전기차").build();
        TagResponseDTO response = carService.registerTag(request);

        assertThat(response.getName()).isEqualTo("전기차");
        assertThat(tagRepository.findAll()).hasSize(1);
    }
    @Test
    @DisplayName("자동차 태그 제거 테스트")
    void removeTagTest() {
        TagResponseDTO tagResponse = carService.registerTag(new TagRequest("8인승"));
        assertThat(tagRepository.findAll()).hasSize(1);

        carService.removeTag(tagResponse.getTagId());

        assertThat(tagRepository.findAll()).isEmpty();
        assertThat(carTagRepository.findAll()).isEmpty();
    }
    @Test
    @DisplayName("태그 필터링 테스트")
    void filterCarsByTagTest() {
        Tag tag = tagRepository.save(new Tag("캠핑카"));
        CarRequest carRequest1 = CarRequest.builder().name("자동차").brand("회사").status("available").tagIds(List.of(tag.getTagId())).build();
        carService.registerCar(carRequest1);
        CarRequest carRequest2 = CarRequest.builder().name("자동차2").brand("회사2").status("available").build();
        carService.registerCar(carRequest2);

        List<CarResponseDTO> filteredCars = carService.filterCarsByTag(tag.getTagId());

        assertThat(filteredCars).hasSize(1);
        assertThat(filteredCars.get(0).getName()).isEqualTo("자동차");
        assertThat(filteredCars.get(0).getTags()).contains("캠핑카");

    }
}
