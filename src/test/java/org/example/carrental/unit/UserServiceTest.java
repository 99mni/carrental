package org.example.carrental.unit;

import lombok.extern.slf4j.Slf4j;
import org.example.carrental.testdouble.FakeUserRepository;
import org.example.carrental.user.controller.request.UserRequest;
import org.example.carrental.user.controller.response.UserResponse;
import org.example.carrental.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("사용자 서비스 테스트")
public class UserServiceTest {

    @Test
    @DisplayName("사용자 등록 테스트")
    public void registerUserTest() {
        //준비
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        UserService userService = new UserService(fakeUserRepository);
        UserRequest userRequest = new UserRequest("TEST_USER");

        //실행
        UserResponse userResponse = userService.registerUser(userRequest);

        //검증
        assertThat(userResponse.getName()).isEqualTo("TEST_USER");
    }

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    public void getAllUsersTest() {
        //준비
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        UserService userService = new UserService(fakeUserRepository);
        userService.registerUser(new UserRequest("TEST_USER_1"));
        userService.registerUser(new UserRequest("TEST_USER_2"));

        //실행
        List<UserResponse> userResponses = userService.getAllUsers();

        //검증
        assertThat(userResponses.size()).isEqualTo(2);
        assertThat(userResponses).extracting("name").containsExactlyInAnyOrder("TEST_USER_1", "TEST_USER_2");
    }

    @Test
    @DisplayName("사용자 조회 테스트")
    public void getUserByIdTest() {
        //준비
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        UserService userService = new UserService(fakeUserRepository);
        UserResponse savedUser = userService.registerUser(new UserRequest("TEST_USER"));

        //실행
        UserResponse foundUser = userService.getUserById(savedUser.getId());

        //검증
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("TEST_USER");
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    public void deleteUserTest() {
        //준비
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        UserService userService = new UserService(fakeUserRepository);
        UserResponse savedUser = userService.registerUser(new UserRequest("TEST_USER"));

        //실행
        userService.deleteUser(savedUser.getId());

        //검증
        assertThat(fakeUserRepository.findById(savedUser.getId())).isEmpty();
    }



}
