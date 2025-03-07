//package org.example.carrental.E2E;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.carrental.car.controller.request.CarRequest;
//import org.example.carrental.car.controller.request.TagRequest;
//import org.example.carrental.car.controller.response.CarResponseDTO;
//import org.example.carrental.car.infrastructure.CarTagRepository;
//import org.example.carrental.car.infrastructure.TagRepository;
//import org.example.carrental.car.service.port.CarRepository;
//import org.example.carrental.rental.controller.reqeust.RentalRequestDTO;
//import org.example.carrental.rental.controller.response.RentalResponseDTO;
//import org.example.carrental.rental.service.port.RentalRepository;
//import org.example.carrental.car.service.CarService;
//import org.example.carrental.user.service.UserService;
//import org.example.carrental.user.controller.request.UserRequest;
//import org.example.carrental.user.controller.response.UserResponse;
//import org.example.carrental.user.infrastructure.UserRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Transactional
//@DisplayName("자동차 E2E 테스트")
//public class E2E {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private CarRepository carRepository;
//    @Autowired private UserRepository userRepository;
//    @Autowired private RentalRepository rentalRepository;
//    @Autowired private CarService carService;
//    @Autowired private UserService userService;
//    @Autowired private ObjectMapper objectMapper;
//
//    private static final String BASE_URL_CAR = "/api/cars/";
//    private static final String BASE_URL_USER = "/api/users/";
//    private static final String BASE_URL_RENTAL = "/api/rentals/";
//
//    private Long carId;
//    private Long userId;
//    @Autowired
//    private TagRepository tagRepository;
//    @Autowired
//    private CarTagRepository carTagRepository;
//
//    @BeforeAll
//    void setUp() throws Exception {
//        rentalRepository.deleteAll();
//        carTagRepository.deleteAll();
//        carRepository.deleteAll();
//        userRepository.deleteAll();
//        tagRepository.deleteAll();
//
//    }
//
//    @Test
//    @Order(1)
//    @DisplayName("E2E 전체 시나리오 테스트 (사용자 등록 → 자동차 등록 → 렌탈 → 반납)")
//    void RentalTest() throws Exception {
//        //사용자 등록
//        UserRequest userRequest = UserRequest.builder().name("user1").build();
//        String userResponse = mockMvc.perform(post(BASE_URL_USER + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequest)))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        userId = objectMapper.readValue(userResponse, UserResponse.class).getId();
//        //자동차 등록
//        CarRequest carRequest = CarRequest.builder().name("car1").brand("brand").build();
//        String carResponse = mockMvc.perform(post(BASE_URL_CAR + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(carRequest)))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        carId = objectMapper.readValue(carResponse, CarResponseDTO.class).getId();
//        // 자동차 렌탈 요청
//        RentalRequestDTO rentalRequest = RentalRequestDTO.builder().carId(carId).userId(userId).rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(5)).status("rented").build();
//
//        String rentalResponse = mockMvc.perform(post(BASE_URL_RENTAL + "rent").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(rentalRequest)))
//                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("rented")).andDo(print()).andReturn().getResponse().getContentAsString();
//
//        RentalResponseDTO rental = objectMapper.readValue(rentalResponse, RentalResponseDTO.class);
//        assertThat(rental.getCarName()).isEqualTo("car1");
//
//        //렌탈 상태 확인
//        mockMvc.perform(get(BASE_URL_RENTAL + "status/" + carId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("rented"))
//                .andDo(print());
//
//        //자동차 반납
//        mockMvc.perform(put(BASE_URL_RENTAL + "return/" + carId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("자동차(ID: " + carId + ") 반납 완료"))
//                .andDo(print());
//
//        //반납 후 상태 확인
//        mockMvc.perform(get(BASE_URL_RENTAL + "status/" + carId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("available"))
//                .andDo(print());
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("자동차 삭제 후 조회 테스트")
//    void deleteAndCheck() throws Exception {
//        //자동차 등록
//        CarRequest carRequest = CarRequest.builder().name("car1").brand("brand").build();
//        String carResponse = mockMvc.perform(post(BASE_URL_CAR + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(carRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        carId = objectMapper.readValue(carResponse, CarResponseDTO.class).getId();
//
//        //자동차 삭제 요청
//        mockMvc.perform(delete(BASE_URL_CAR +"delete/"+ carId)).andExpect(status().isNoContent()).andDo(print());
//
//        //자동차 조회
//        mockMvc.perform(get(BASE_URL_CAR + carId)).andExpect(status().isInternalServerError()).andExpect(content().string(containsString("서버 오류: 해당 ID의 자동차를 찾을 수 없음"))).andDo(print());
//    }
//    @Test
//    @Order(3)
//    @DisplayName("자동차 검색 테스트 (이름, 제조사)")
//    void searchCarByNameAndBrand() throws Exception {
//        //사용자 등록
//        UserRequest userRequest = UserRequest.builder().name("user1").build();
//        String userResponse = mockMvc.perform(post(BASE_URL_USER + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        userId = objectMapper.readValue(userResponse, UserResponse.class).getId();
//        //자동차 등록
//        CarRequest carRequest = CarRequest.builder().name("car1").brand("brand").build();
//        String carResponse = mockMvc.perform(post(BASE_URL_CAR + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(carRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        carId = objectMapper.readValue(carResponse, CarResponseDTO.class).getId();
//
//        // 자동차 이름으로 검색
//        mockMvc.perform(get(BASE_URL_CAR + "search/model").param("keyword", "car")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].name").value("car1")).andDo(print());
//
//        // 자동차 제조사로 검색
//        mockMvc.perform(get(BASE_URL_CAR + "search/brand").param("keyword", "brand")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].brand").value("brand")).andDo(print());
//
//        // 존재하지 않는 이름으로 검색 (결과 없음)
//        mockMvc.perform(get(BASE_URL_CAR + "search/model").param("keyword", "Nonexistent")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$.length()").value(0)).andDo(print());
//    }
//
//
//    @Test
//    @Order(4)
//    @DisplayName("렌탈 중인 차량 재렌탈 방지 테스트")
//    void notAllowRentalForRentedCar() throws Exception {
//        //사용자 등록
//        UserRequest userRequest = UserRequest.builder().name("user1").build();
//        String userResponse = mockMvc.perform(post(BASE_URL_USER + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        userId = objectMapper.readValue(userResponse, UserResponse.class).getId();
//        //자동차 등록
//        CarRequest carRequest = CarRequest.builder().name("car1").brand("brand").build();
//        String carResponse = mockMvc.perform(post(BASE_URL_CAR + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(carRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        carId = objectMapper.readValue(carResponse, CarResponseDTO.class).getId();
//
//        // 첫 번째 사용자가 차량 렌탈
//        RentalRequestDTO rentalRequest = RentalRequestDTO.builder().carId(carId).userId(userId).rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(5)).status("rented").build();
//        mockMvc.perform(post(BASE_URL_RENTAL + "rent").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(rentalRequest))).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("rented")).andDo(print());
//
//        // 사용자 2명 등록
//        UserRequest user2Request = UserRequest.builder().name("user2").build();
//        String user2Response = mockMvc.perform(post(BASE_URL_USER + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user2Request))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        Long user2Id = objectMapper.readValue(user2Response, UserResponse.class).getId();
//
//        // 두 번째 사용자 동일 차량 렌탈
//        RentalRequestDTO rentalRe = RentalRequestDTO.builder().carId(carId).userId(user2Id).rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(5)).status("rented").build();
//        mockMvc.perform(post(BASE_URL_RENTAL + "rent").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(rentalRequest))).andExpect(status().isBadRequest()).andExpect(content().string(containsString("렌탈 중인 차량입니다."))).andDo(print());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("자동차 태그 추가 및 필터링 테스트")
//    void addTagAndFilterCar() throws Exception {
//        //자동차 등록
//        CarRequest carRequest = CarRequest.builder().name("car1").brand("brand").build();
//        String carResponse = mockMvc.perform(post(BASE_URL_CAR + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(carRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        carId = objectMapper.readValue(carResponse, CarResponseDTO.class).getId();
//        // 자동차에 태그 추가
//        TagRequest tagRequest = TagRequest.builder().name("소형차").build();
//        String tagResponse = mockMvc.perform(post(BASE_URL_CAR+ "tags").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new TagRequest("소형차")))).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("소형차")).andDo(print()).andReturn().getResponse().getContentAsString();
//        Long tagId = objectMapper.readValue(tagResponse, TagResponseDTO.class).getTagId();
//
//        // 자동차에 태그 연결
//        mockMvc.perform(post(BASE_URL_CAR + carId + "/tags").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(new TagRequest("소형차")))).andExpect(status().isOk()).andExpect(jsonPath("$.tags").value("소형차")).andDo(print());
//
//        // 필터링 요청
//        mockMvc.perform(get(BASE_URL_CAR + "filter/" + tagId)).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("car1")).andDo(print());
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("잘못된 데이터 입력 시 검증 테스트")
//    void invalidDataValidation() throws Exception {
//        //사용자 등록
//        UserRequest userRequest = UserRequest.builder().name("user1").build();
//        String userResponse = mockMvc.perform(post(BASE_URL_USER + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        userId = objectMapper.readValue(userResponse, UserResponse.class).getId();
//        //자동차 잘못된 데이터 등록
//        CarRequest invalidCarRequest = CarRequest.builder().name("").brand("brand").build();
//
//        //오류메세지 출력
//        mockMvc.perform(post(BASE_URL_CAR + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidCarRequest))).andExpect(status().isInternalServerError()).andExpect(content().string(containsString("자동차 이름은 필수입니다."))).andDo(print());
//
//        // 사용자 잘못된 데이터 등록
//        UserRequest invalidUserRequest = UserRequest.builder().name("").build();
//
//        //오류메세지 출력
//        mockMvc.perform(post(BASE_URL_USER + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidUserRequest))).andExpect(status().isBadRequest()).andExpect(content().string(containsString("이름은 필수 항목"))).andDo(print());
//    }
//
//    @Test
//    @Order(6)
//    @DisplayName("존재하지 않는 자동차 렌탈")
//    void rentNonExistingCar() throws Exception {
//        //사용자 등록
//        UserRequest userRequest = UserRequest.builder().name("user1").build();
//        String userResponse = mockMvc.perform(post(BASE_URL_USER + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        userId = objectMapper.readValue(userResponse, UserResponse.class).getId();
//
//        //렌탈 등록
//        RentalRequestDTO rentalRequest = RentalRequestDTO.builder().carId(999L).userId(userId).rentalDate(LocalDate.now()).returnDate(LocalDate.now().plusDays(3)).status("rented").build();
//
//        //오류 메세지 출력
//        mockMvc.perform(post(BASE_URL_RENTAL + "rent").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(rentalRequest))).andExpect(status().isNotFound()).andExpect(content().string("리소스를 찾을 수 없습니다: 자동차 ID 999 를 찾을 수 없음")).andDo(print());
//    }
//    @Test
//    @Order(7)
//    @DisplayName("존재하지 않는 사용자 조회 테스트")
//    void getNonExistingUser() throws Exception {
//
//        mockMvc.perform(get(BASE_URL_USER + "999")).andExpect(status().isInternalServerError()).andExpect(content().string(containsString("해당 ID의 사용자를 찾을 수 없음"))).andDo(print());
//    }
//
//}
