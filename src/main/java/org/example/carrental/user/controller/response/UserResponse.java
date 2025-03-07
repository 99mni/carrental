package org.example.carrental.user.controller.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.carrental.user.domain.User;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;

    @NotBlank(message = "이름 입력")
    private String name;

    public UserResponse(User user) {
        this.name = user.getName();
        this.id = user.getId();
    }
}
