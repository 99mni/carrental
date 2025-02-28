package org.example.carrental.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarTagResponseDTO {
    private Long carId;
    private String carName;
    private String carBrand;
    private String carStatus;
    private Long tagId;
    private String tagName;

}
