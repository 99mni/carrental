package org.example.carrental.user.service;

import lombok.RequiredArgsConstructor;
import org.example.carrental.user.controller.request.UserRequest;
import org.example.carrental.user.controller.response.UserResponse;
import org.example.carrental.user.domain.User;
import org.example.carrental.user.service.port.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse registerUser(UserRequest userRequest){
        User user = User.builder().name(userRequest.getName()).build();
        User savedUser = userRepository.saveUser(user);
        return new UserResponse(savedUser);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id).map(UserResponse::new)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자를 찾을 수 없음"));
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("해당 사용자를 찾을 수 없음");
        }
        userRepository.deleteById(id);
    }
}
