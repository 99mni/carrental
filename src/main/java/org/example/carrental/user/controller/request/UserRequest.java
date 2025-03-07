package org.example.carrental.user.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "이름은 필수 항목")
    private String name;
}