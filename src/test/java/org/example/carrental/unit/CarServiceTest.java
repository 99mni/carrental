package org.example.carrental.unit;

import org.example.carrental.car.controller.request.CarRequest;
import org.example.carrental.car.controller.request.TagRequest;
import org.example.carrental.car.controller.response.CarResponse;
import org.example.carrental.car.controller.response.TagResponse;
import org.example.carrental.car.domain.Car;
import org.example.carrental.car.service.CarService;
import org.example.carrental.testdouble.FakeCarRepository;
import org.example.carrental.testdouble.FakeTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("자동차 서비스 테스트")
public class CarServiceTest {

    @Test
    @DisplayName("자동차 등록 테스트")
    void registerCarTest() {
        //준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        CarRequest carRequest = new CarRequest("NAME_TEST", "BRAND_TEST");

        //실행
        CarResponse savedCar = carService.registerCar(carRequest);

        //검증
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getName()).isEqualTo("NAME_TEST");
        assertThat(savedCar.getBrand()).isEqualTo("BRAND_TEST");
        assertThat(savedCar.getStatus()).isEqualTo("available");
    }

    @Test
    @DisplayName("모든 자동차 조회 테스트")
    void getAllCarsTest(){
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        carService.registerCar(new CarRequest("NAME_TEST", "BRAND_TEST"));
        carService.registerCar(new CarRequest("NAME_TEST2", "BRAND_TEST2"));

        // 실행
        List<CarResponse> cars = carService.getAllCars();

        // 검증
        assertThat(cars).hasSize(2);
        assertThat(cars).extracting("name").containsExactlyInAnyOrder("NAME_TEST", "NAME_TEST2");
    }

    @Test
    @DisplayName("ID 자동차 조회")
    void getCarByIdTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        CarResponse savedCar = carService.registerCar(new CarRequest("NAME_TEST", "BRAND_TEST"));

        // 실행
        CarResponse foundCar = carService.getCarById(savedCar.getId());

        // 검증
        assertThat(foundCar).isNotNull();
        assertThat(foundCar.getName()).isEqualTo("NAME_TEST");
    }

    @Test
    @DisplayName("자동차 삭제 테스트")
    void deleteCarTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        CarResponse savedCar = carService.registerCar(new CarRequest("NAME_TEST", "BRAND_TEST"));

        // 실행
        carService.deleteCar(savedCar.getId());

        // 검증
        assertThat(fakeCarRepository.findById(savedCar.getId())).isEmpty();
    }

    @Test
    @DisplayName("자동차 이름으로 검색")
    void getCarsByNameTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        carService.registerCar(new CarRequest("NAME_TEST", "BRAND_TEST"));
        carService.registerCar(new CarRequest("NAME_TEST2", "BRAND_TEST2"));

        // 실행
        List<CarResponse> cars = carService.getCarsByNameContaining("NAME_TEST");

        // 검증
        assertThat(cars).hasSize(2);
    }

    @Test
    @DisplayName("회사명으로 검색")
    void getCarsByBrandTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        carService.registerCar(new CarRequest("NAME_TEST", "BRAND_TEST"));
        carService.registerCar(new CarRequest("NAME_TEST2", "BRAND_TEST2"));

        // 실행
        List<CarResponse> cars = carService.getCarsByBrandContaining("BRAND_TEST");

        // 검증
        assertThat(cars).hasSize(2);
        assertThat(cars.get(0).getBrand()).isEqualTo("BRAND_TEST");
    }

    @Test
    @DisplayName("자동차 수정 테스트")
    void updateCarTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        CarResponse savedCar = carService.registerCar(new CarRequest("NAME_TEST", "BRAND_TEST"));
        TagResponse newTag = carService.registerTag(new TagRequest("TAG_TEST"));
        CarRequest updateRequest = new CarRequest("NAME_NEW_TEST", "BRAND_NEW_TEST", "rented", List.of(newTag.getName()));

        // 실행
        CarResponse updatedCar = carService.updateCar(savedCar.getId(), updateRequest);

        // 검증
        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar.getName()).isEqualTo("NAME_NEW_TEST");
        assertThat(updatedCar.getBrand()).isEqualTo("BRAND_NEW_TEST");
        assertThat(updatedCar.getStatus()).isEqualTo("rented");
        assertThat(updatedCar.getTags()).contains("TAG_TEST");
    }

    @Test
    @DisplayName("자동차 배치 저장 테스트")
    void saveCarFromTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        List<Car> cars = List.of(
                new Car(null, "NAME_TEST", "BRAND_TEST", "available"),
                new Car(null, "NAME_TEST2", "BRAND_TEST2", "available")
        );

        // 실행
        carService.saveCarsFrom(cars);

        // 검증
        List<CarResponse> allCars = carService.getAllCars();
        assertThat(allCars).hasSize(2);
        assertThat(allCars).extracting("name").containsExactlyInAnyOrder("NAME_TEST", "NAME_TEST2");
    }
    @Test
    @DisplayName("빈 자동차 리스트 저장 예외 테스트")
    void saveCarsFromEmptyListTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        List<Car> emptyList = List.of();

        // 실행 & 검증
        assertThatThrownBy(() -> carService.saveCarsFrom(emptyList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록할 자동차 데이터가 없음");
    }

    @Test
    @DisplayName("자동차 태그 추가 테스트")
    void registerTag() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        TagRequest request = new TagRequest("TAG_TEST");

        // 실행
        TagResponse response = carService.registerTag(request);

        // 검증
        assertThat(response.getName()).isEqualTo("TAG_TEST");
    }
    @Test
    @DisplayName("태그 필터링 테스트")
    void filterCarsByTagTest() {
        // 준비
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeTagRepository fakeTagRepository = new FakeTagRepository();
        CarService carService = new CarService(fakeCarRepository, fakeTagRepository);
        TagResponse tagResponse = carService.registerTag(new TagRequest("TAG_TEST"));
        CarRequest carRequest1 = new CarRequest("NAME_TEST", "BRAND_TEST", "available", List.of(tagResponse.getName()));
        CarRequest carRequest2 = new CarRequest("NAME_TEST2", "BRAND_TEST2", "available");
        carService.registerCar(carRequest1);
        carService.registerCar(carRequest2);

        // 실행
        List<CarResponse> filteredCars = carService.filterCarsByTag(tagResponse.getName());

        // 검증
        assertThat(filteredCars).hasSize(1);
        assertThat(filteredCars.get(0).getName()).isEqualTo("NAME_TEST");
        assertThat(filteredCars.get(0).getTags()).contains("TAG_TEST");
    }




}
