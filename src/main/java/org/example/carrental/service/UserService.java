package org.example.carrental.service;

import lombok.RequiredArgsConstructor;
import org.example.carrental.DTO.UserRequestDTO;
import org.example.carrental.DTO.UserResponseDTO;
import org.example.carrental.entity.User;
import org.example.carrental.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO){
        User user = User.builder().name(userRequestDTO.getName()).build();
        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id).map(UserResponseDTO::new)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자를 찾을 수 없음"));
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("해당 사용자를 찾을 수 없음");
        }
        userRepository.deleteById(id);
    }
}
