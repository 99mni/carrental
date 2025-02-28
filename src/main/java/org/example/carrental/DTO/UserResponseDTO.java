package org.example.carrental.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.carrental.entity.User;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;

    @NotBlank(message = "이름 입력")
    private String name;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
