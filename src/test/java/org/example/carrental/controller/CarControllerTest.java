//package org.example.carrental.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.example.carrental.CarrentalApplication;
//import org.example.carrental.car.controller.request.CarRequest;
//import org.example.carrental.car.controller.response.CarResponseDTO;
//import org.example.carrental.car.controller.request.TagRequest;
//import org.example.carrental.car.domain.CarTag;
//import org.example.carrental.car.domain.Tag;
//import org.example.carrental.car.service.port.CarRepository;
//import org.example.carrental.car.infrastructure.CarTagRepository;
//import org.example.carrental.car.infrastructure.TagRepository;
//import org.example.carrental.car.service.CarService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.ByteArrayOutputStream;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@DisplayName("자동차 컨트롤러 테스트")
//@SpringBootTest(classes = CarrentalApplication.class)
//@AutoConfigureMockMvc
//public class CarControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private CarService carService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private TagRepository tagRepository;
//
//    @Autowired
//    private CarRepository carRepository;
//
//    private static final String BASE_URL = "/api/cars/";
//
//    @Autowired
//    private CarTagRepository carTagRepository;
//
//    @BeforeEach
//    void setUp() {
//        carTagRepository.deleteAll();
//        tagRepository.deleteAll();
//        carRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("자동차 등록")
//    void registerCarTest() throws Exception {
//        CarRequest request = CarRequest.builder().name("자동차_테스트").brand("회사_테스트").status("available").build();
//
//        mockMvc.perform(post(BASE_URL + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("자동차_테스트")).andExpect(jsonPath("$.brand").value("회사_테스트"));
//    }
//
//    @Test
//    @DisplayName("자동차 조회")
//    void getAllCarsTest() throws Exception {
//        CarRequest request = CarRequest.builder().name("자동차_테스트").brand("회사_테스트").status("available").build();
//        carService.registerCar(request);
//
//        mockMvc.perform(get(BASE_URL + "all"))
//                .andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("자동차_테스트"));
//    }
//
//    @Test
//    @DisplayName("자동차 검색_이름")
//    void searchCarsByModelTest() throws Exception {
//        CarRequest request = CarRequest.builder().name("자동차_테스트").brand("회사_테스트").status("available").build();
//        carService.registerCar(request);
//
//        mockMvc.perform(get(BASE_URL + "search/model").param("keyword", "자동차"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("자동차_테스트"));
//    }
//    @Test
//    @DisplayName("자동차 검색_회사")
//    void searchCarsByBrandTest() throws Exception {
//        CarRequest request = CarRequest.builder().name("자동차_테스트").brand("회사_테스트").status("available").build();
//        carService.registerCar(request);
//
//
//        mockMvc.perform(get(BASE_URL + "search/brand").param("keyword", "회사"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].brand").value("회사_테스트"));
//    }
//
//    @Test
//    @DisplayName("특정 자동차 렌탈 상태 확인")
//    void getCarByIdTest() throws Exception {
//        CarRequest request = CarRequest.builder().name("자동차_테스트").brand("회사_테스트").status("available").build();
//        CarResponseDTO savedCar = carService.registerCar(request);
//
//        mockMvc.perform(get(BASE_URL + savedCar.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("available"));
//    }
//
//    @Test
//    @DisplayName("자동차 삭제")
//    void deleteCarTest() throws Exception {
//        CarRequest request = CarRequest.builder().name("자동차_테스트").brand("회사_테스트").status("available").build();
//        CarResponseDTO carResponseDTO = carService.registerCar(request);
//
//        mockMvc.perform(delete(BASE_URL + "delete/" + carResponseDTO.getId()))
//                .andExpect(status().isNoContent());
//
//        Optional<Car> deletedCar = carRepository.findById(carResponseDTO.getId());
//        assertThat(deletedCar).isEmpty();
////        assertThat(carService.getAllCars()).isEmpty();
//    }
//
//    @Test
//    @DisplayName("자동차 수정")
//    void updateCarTest() throws Exception {
//        CarRequest initialRequest = CarRequest.builder().name("초기_자동차").brand("초기_회사").status("available").build();
//        CarResponseDTO savedCar = carService.registerCar(initialRequest);
//        Tag tag = tagRepository.save(new Tag("스포츠"));
//        CarRequest updatedRequest = CarRequest.builder().name("수정_자동차").brand("수정_회사").status("rented").tagIds(List.of(tag.getTagId())).build();
//
//        mockMvc.perform(put(BASE_URL + "update/" + savedCar.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("수정_자동차"))
//                .andExpect(jsonPath("$.brand").value("수정_회사"))
//                .andExpect(jsonPath("$.tags[0]").value("스포츠"));
//    }
//
//    @Transactional
//    @Test
//    @DisplayName("태그 필터링")
//    void filterCarsByTagTest() throws Exception {
//        Tag tag1 = tagRepository.findByName("1인승").orElseGet(() ->tagRepository.save(new Tag("1인승")));
//        Tag tag2 = tagRepository.findByName("2인승").orElseGet(() ->tagRepository.save(new Tag("2인승")));
//
//        Car car1 = carRepository.save(Car.builder().name("자동차1").brand("회사1").status("available").build());
//        Car car2 = carRepository.save(Car.builder().name("자동차2").brand("회사2").status("available").build());
//        carRepository.save(Car.builder().name("자동차3").brand("회사3").status("available").build());
//
//        carTagRepository.save(new CarTag(car1, tag1));
//        carTagRepository.save(new CarTag(car2, tag2));
//
//        mockMvc.perform(get(BASE_URL + "filter/" + tag1.getTagId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("$[0].name").value("자동차1"))
//                .andExpect(jsonPath("$[0].brand").value("회사1"));
//
//        mockMvc.perform(get(BASE_URL + "filter/" + tag2.getTagId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1))
//                .andExpect(jsonPath("$[0].name").value("자동차2"))
//                .andExpect(jsonPath("$[0].brand").value("회사2"));
//    }
//    @Test
//    @DisplayName("태그 등록")
//    void addTagToCarTest() throws Exception {
//        TagRequest tagRequest = new TagRequest("전기차");
//
//        mockMvc.perform(post(BASE_URL + "tags").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(tagRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("전기차"));
//    }
//    @Test
//    @DisplayName("중복 태그 등록 시 예외 처리 테스트")
//    void createDuplicateTagTest() throws Exception {
//        tagRepository.save(new Tag("SUV"));
//        TagRequest duplicateRequest = new TagRequest("SUV");
//
//        mockMvc.perform(post(BASE_URL + "tags").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(duplicateRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("이미 존재하는 태그"));
//    }
//    @Test
//    @DisplayName("태그 삭제")
//    void removeTagFromCarTest() throws Exception {
//        Tag tag = tagRepository.save(new Tag("SUV"));
//
//        mockMvc.perform(delete(BASE_URL + "tags/" + tag.getTagId())).andExpect(status().isNoContent());
//
//        assertThat(tagRepository.findById(tag.getTagId())).isEmpty();
//    }
//
//
//    @Test
//    @DisplayName("배치 파일")
//    void batchUploadTest() throws Exception {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Cars");
//        Row header = sheet.createRow(0);
//        header.createCell(0).setCellValue("name");
//        header.createCell(1).setCellValue("brand");
//        header.createCell(2).setCellValue("status");
//        Row row = sheet.createRow(1);
//        row.createCell(0).setCellValue("자동차");
//        row.createCell(1).setCellValue("회사");
//        row.createCell(2).setCellValue("available");
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        workbook.write(out);
//        workbook.close();
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "Cars.xlsx",
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
//                out.toByteArray()
//        );
//
//        mockMvc.perform(multipart(BASE_URL + "batch").file(file))
//                .andExpect(status().isOk())
//                .andExpect(content().string("배치 등록이 성공적으로 완료되었습니다."));
//
//    }
//
//
//
//
//}
