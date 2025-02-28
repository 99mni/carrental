package org.example.carrental.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.carrental.entity.Car;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class CarResponseDTO {
    private Long id;

    @NotBlank(message = "자동차명 입력")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "회사명 입력")
    private String brand;

    private String status;

    private List<String> tags;

    public CarResponseDTO(Car car) {
        this.id = car.getId();
        this.name = car.getName();
        this.brand = car.getBrand();
        this.status = car.getStatus();
    }

    public static CarResponseDTO fromEntity(Car car) {
        return CarResponseDTO.builder()
                .id(car.getId())
                .name(car.getName())
                .brand(car.getBrand())
                .status(car.getStatus())
                .tags(car.getCarTags() != null ? car.getCarTags().stream().map(carTag -> carTag.getTag().getName()).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }


}
