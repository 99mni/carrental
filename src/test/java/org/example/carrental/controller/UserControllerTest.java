//package org.example.carrental.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.carrental.user.controller.request.UserRequest;
//import org.example.carrental.user.infrastructure.UserRepository;
//import org.example.carrental.user.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@DisplayName("사용자 컨트롤러 테스트")
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("사용자 등록")
//    void registerUserTest() throws Exception {
//        UserRequest userRequest = UserRequest.builder().name("사용자").build();
//
//        mockMvc.perform(post(BASE_URL + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRequest)))
//                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("사용자"));
//    }
//    @Test
//    @DisplayName("사용자 등록_실패")
//    void registerUserFailTest() throws Exception {
//        UserRequest invalidUser = UserRequest.builder().name("").build();
//
//        mockMvc.perform(post(BASE_URL + "register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidUser)))
//                .andExpect(status().isBadRequest()).andExpect(content().string("이름은 필수 항목"));
//    }
//
//    @Test
//    @DisplayName("사용자 조회")
//    void getAllUsersTest() throws Exception {
//        userService.registerUser(UserRequest.builder().name("사용자1").build());
//        userService.registerUser(UserRequest.builder().name("사용자2").build());
//
//        mockMvc.perform(get(BASE_URL + "all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].name").value("사용자1"))
//                .andExpect(jsonPath("$[1].name").value("사용자2"));
//    }
//
//    @Test
//    @DisplayName("특정 사용자 조회")
//    void getUserByIdTest() throws Exception {
//        Long userId = userService.registerUser(UserRequest.builder().name("사용자").build()).getId();
//
//        mockMvc.perform(get(BASE_URL + userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("사용자"));
//    }
//
//    @Test
//    @DisplayName("사용자 삭제 테스트")
//    void deleteUserTest() throws Exception {
//        Long userId = userService.registerUser(UserRequest.builder().name("사용자").build()).getId();
//
//        mockMvc.perform(delete(BASE_URL + "delete/" + userId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("사용자가 성공적으로 삭제되었습니다."));
//
//        assertThat(userRepository.findById(userId)).isEmpty();
//    }
//
//}
