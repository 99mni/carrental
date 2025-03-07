package org.example.carrental.unit;

import org.example.carrental.car.domain.Car;
import org.example.carrental.rental.controller.reqeust.RentalRequest;
import org.example.carrental.rental.controller.response.RentalResponse;
import org.example.carrental.rental.domain.Rental;
import org.example.carrental.rental.service.RentalService;
import org.example.carrental.testdouble.FakeCarRepository;
import org.example.carrental.testdouble.FakeRentalRepository;
import org.example.carrental.testdouble.FakeUserRepository;
import org.example.carrental.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("렌탈 서비스 테스트")
public class RentalServiceTest {

    @Test
    @DisplayName("차 렌트 성공")
    void rentCar_Success() {
        //준비
        FakeRentalRepository fakeRentalRepository = new FakeRentalRepository();
        FakeCarRepository fakeCarRepository = new FakeCarRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        RentalService rentalService = new RentalService(fakeRentalRepository, fakeCarRepository, fakeUserRepository);
        User user = new User(1L, "USER_TEST");
        Car car = new Car(1L, "CAR_TEST", "BRAND_TEST", "available");
        fakeUserRepository.saveUser(user);
        fakeCarRepository.saveCar(car);
        RentalRequest rentalRequest = new RentalRequest(user.getId(), car.getId(), LocalDate.now(), LocalDate.now().plusDays(5), "rented");

        //실행
        Rental rental = rentalService.rentCar(rentalRequest);

        //검증
        assertThat(rental).isNotNull();
        assertThat(rental.getStatus()).isEqualTo("rented");
    }

    @Test
    @DisplayName("모든 렌탈 조회 테스트")
    void getAllActiveRentalsTest() {
        // 준비
        FakeRentalRepository rentalRepository = new FakeRentalRepository();
        FakeCarRepository carRepository = new FakeCarRepository();
        FakeUserRepository userRepository = new FakeUserRepository();
        RentalService rentalService = new RentalService(rentalRepository, carRepository, userRepository);
        rentalRepository.save(new Rental(1L, new Car(1L, "CAR_TEST", "BRAND_TEST", "available"), new User(1L, "USER_TEST"), LocalDate.now(), LocalDate.now().plusDays(5), "rented"));

        // 실행
        List<RentalResponse> rentals = rentalService.getAllActiveRentals();

        // 검증
        assertThat(rentals).hasSize(1);
    }

    @Test
    @DisplayName("렌탈 상태 조회 테스트 - 사용 가능")
    void getRentalStatusAvailableTest() {
        // 준비
        FakeCarRepository carRepository = new FakeCarRepository();
        carRepository.saveCar(new Car(1L, "CAR_TEST", "BRAND_TEST", "available"));
        RentalService rentalService = new RentalService(new FakeRentalRepository(), carRepository, new FakeUserRepository());

        // 실행 & 검증
        assertThat(rentalService.getRentalStatus(1L)).isEqualTo("available");
    }

    @Test
    @DisplayName("렌탈 상태 조회 테스트 - 대여 중")
    void getRentalStatusRentedTest() {
        // 준비
        FakeCarRepository carRepository = new FakeCarRepository();
        carRepository.saveCar(new Car(1L, "CAR_TEST", "BRAND_TEST", "rented"));
        RentalService rentalService = new RentalService(new FakeRentalRepository(), carRepository, new FakeUserRepository());

        // 실행 & 검증
        assertThat(rentalService.getRentalStatus(1L)).isEqualTo("rented");
    }

    @Test
    @DisplayName("자동차 반납 테스트")
    void returnCarByCarIdTest() {
        // 준비
        FakeRentalRepository rentalRepository = new FakeRentalRepository();
        FakeCarRepository carRepository = new FakeCarRepository();
        FakeUserRepository userRepository = new FakeUserRepository();
        RentalService rentalService = new RentalService(rentalRepository, carRepository, userRepository);
        Car car = new Car(1L, "CAR_TEST", "BRAND_TEST", "rented");
        User user = new User(1L, "USER_TEST");
        Rental rental = new Rental(1L, car, user, LocalDate.now(), LocalDate.now().plusDays(5), "rented");
        rentalRepository.save(rental);

        // 실행
        rentalService.returnCarByCarId(car.getId());

        // 검증
        assertThat(carRepository.findById(car.getId())).isPresent()
                .get()
                .extracting(Car::getStatus)
                .isEqualTo("available");
    }
}
