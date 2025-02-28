package org.example.carrental.service;

import org.example.carrental.DTO.UserRequestDTO;
import org.example.carrental.DTO.UserResponseDTO;
import org.example.carrental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 서비스 테스트")
@SpringBootTest
@Transactional//테스트가 끝나면 강제로 롤백
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 등록 테스트")
    void registerUserTest() {
        UserRequestDTO userRequestDTO = UserRequestDTO.builder().name("사용자_테스트").build();

        UserResponseDTO savedUser = userService.registerUser(userRequestDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("사용자_테스트");
        assertThat(userRepository.findById(savedUser.getId())).isPresent();
    }

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    void getAllUsersTest() {
        userService.registerUser(UserRequestDTO.builder().name("사용자1").build());
        userService.registerUser(UserRequestDTO.builder().name("사용자2").build());

        List<UserResponseDTO> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
        assertThat(users).extracting("name").containsExactlyInAnyOrder("사용자1", "사용자2");
    }

    @Test
    @DisplayName("사용자 조회")
    void getUserByIdTest() {
        UserResponseDTO savedUserDTO = userService.registerUser(UserRequestDTO.builder().name("사용자").build());

        UserResponseDTO foundUserDTO = userService.getUserById(savedUserDTO.getId());

        assertThat(foundUserDTO).isNotNull();
        assertThat(foundUserDTO.getName()).isEqualTo("사용자");
    }


    @Test
    @DisplayName("사용자 삭제 테스트")
    void deleteUserTest() {
        UserResponseDTO savedUserDTO = userService.registerUser(UserRequestDTO.builder().name("사용자").build());

        userService.deleteUser(savedUserDTO.getId());

        assertThat(userRepository.findById(savedUserDTO.getId())).isEmpty();
    }
}
