package org.example.carrental.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.carrental.DTO.UserRequestDTO;
import org.example.carrental.DTO.UserResponseDTO;
import org.example.carrental.entity.User;
import org.example.carrental.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "사용자 등록")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(userService.registerUser(userRequestDTO));
    }
    @GetMapping("/all")
    @Operation(summary = "사용자 조회")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 사용자 조회")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "사용자 삭제")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
    }



}
