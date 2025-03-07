//package org.example.carrental.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.carrental.CarrentalApplication;
//import org.example.carrental.rental.controller.reqeust.RentalRequestDTO;
//import org.example.carrental.user.domain.User;
//import org.example.carrental.car.service.port.CarRepository;
//import org.example.carrental.rental.service.port.RentalRepository;
//import org.example.carrental.user.infrastructure.UserRepository;
//import org.example.carrental.rental.service.RentalService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@DisplayName("렌탈 컨트롤러 테스트")
//@SpringBootTest(classes = CarrentalApplication.class)
//@AutoConfigureMockMvc
//public class RentalControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CarRepository carRepository;
//
//    @Autowired
//    private RentalRepository rentalRepository;
//
//    @Autowired
//    private RentalService rentalService;
//
//    private static final String BASE_URL = "/api/rentals/";
//
//    @BeforeEach
//    void setUp() {
//        rentalRepository.deleteAll();
//        userRepository.deleteAll();
//        carRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("렌탈 등록")
//    void registerUserTest() throws Exception {
//        User user = userRepository.save(User.builder().name("사용자").build());
//        Car car = carRepository.save(Car.builder().name("렌탈 자동차").brand("회사").status("available").carTags(new ArrayList<>()).build());
//        String rentalRequestJson = objectMapper.writeValueAsString(RentalRequestDTO.builder().carId(car.getId()).userId(user.getId()).rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(3)).status("rented").build());
//
//        mockMvc.perform(post(BASE_URL + "rent").contentType(MediaType.APPLICATION_JSON)
//                        .content(rentalRequestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.carName").value("렌탈 자동차"))
//                .andExpect(jsonPath("$.userName").value("사용자"));
//    }
//    @Test
//    @DisplayName("렌탈 등록 실패 테스트 - 유효성 검사 실패")
//    void registerRentalFailTest() throws Exception {
//        RentalRequestDTO invalidRental = RentalRequestDTO.builder().build();
//
//        mockMvc.perform(post(BASE_URL + "rent")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRental)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("렌탈 상태 전체 확인 테스트")
//    void getAllActiveRentalsTest() throws Exception {
//        User user = userRepository.save(User.builder().name("사용자").build());
//        Car car = carRepository.save(Car.builder().name("렌탈 자동차").brand("회사").status("available").build());
//        rentalService.rentCar(RentalRequestDTO.builder().carId(car.getId()).userId(user.getId()).status("rented").build());
//
//        mockMvc.perform(get(BASE_URL + "all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].carName").value("렌탈 자동차"));
//    }
//
//    @Test
//    @DisplayName("특정 자동차 렌탈 상태 확인 테스트")
//    void getRentalStatusTest() throws Exception {
//        User user = userRepository.save(User.builder().name("사용자").build());
//        Car car = carRepository.save(Car.builder().name("렌탈 자동차").brand("회사").status("available").build());
//        rentalService.rentCar(RentalRequestDTO.builder().carId(car.getId()).userId(user.getId()).status("rented").build());
//
//        mockMvc.perform(get(BASE_URL + "status/" + car.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("rented"));
//    }
//
//    @Test
//    @DisplayName("자동차 반납 테스트")
//    void returnCarByCarIdTest() throws Exception {
//        User user = userRepository.save(User.builder().name("사용자").build());
//        Car car = carRepository.save(Car.builder().name("렌탈 자동차").brand("회사").status("available").build());
//        rentalService.rentCar(RentalRequestDTO.builder().carId(car.getId()).userId(user.getId()).status("rented").build());
//
//        mockMvc.perform(put(BASE_URL + "return/" + car.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().string("자동차(ID: " + car.getId() + ") 반납 완료"));
//
//        assertThat(rentalService.getRentalStatus(car.getId())).isEqualTo("available");
//    }
//}
