package org.example.carrental.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.carrental.user.controller.request.UserRequest;
import org.example.carrental.user.service.UserService;
import org.example.carrental.user.controller.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @Operation(summary = "사용자 등록")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(userService.registerUser(userRequest));
    }
    @GetMapping("/users")
    @Operation(summary = "사용자 조회")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("user/{id}")
    @Operation(summary = "특정 사용자 조회")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("user/{id}")
    @Operation(summary = "사용자 삭제")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
    }



}
