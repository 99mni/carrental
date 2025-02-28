package org.example.carrental.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.carrental.entity.Rental;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalResponseDTO {
    private Long rentalId;
    private String carName;
    private String carBrand;
    private String userName;

    @NotNull(message = "렌탈 날짜를 입력해야 합니다.")
    private LocalDate rentalDate;

    @FutureOrPresent(message = "반납 날짜는 현재 날짜이거나 미래여야 합니다.")
    @NotNull(message = "반납 날짜를 입력해야 합니다.")
    private LocalDate returnDate;

    private String status;

    public RentalResponseDTO(Rental rental) {
        this.rentalId = rental.getId();
        this.carName = rental.getCar().getName();
        this.carBrand = rental.getCar().getBrand();
        this.userName = rental.getUser().getName();
        this.rentalDate = rental.getRentalDate();
        this.returnDate = rental.getReturnDate();
        this.status = rental.getStatus();
    }




}
