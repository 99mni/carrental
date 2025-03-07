package org.example.carrental.rental.controller.reqeust;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalRequest {
    @NotNull(message = "자동차 ID는 필수입니다.")
    private Long carId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "렌탈 날짜를 입력해야 합니다.")
    private LocalDate rentalDate;

    @FutureOrPresent(message = "반납 날짜는 현재 날짜이거나 미래여야 합니다.")
    @NotNull(message = "반납 날짜를 입력해야 합니다.")
    private LocalDate returnDate;

    private String status;
}
