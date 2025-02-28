package org.example.carrental.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Rental {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonBackReference //순환참조 방지
    @NotNull(message = "자동차 정보는 필수")
    @Valid
    private Car car;

    @ManyToOne
    @NotNull(message = "사용자 정보는 필수")
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-rentals")
    @Valid
    private User user;

    private LocalDate rentalDate;
    private LocalDate returnDate;
    private String status;

    public Rental updateStatus(String status) {
        return Rental.builder()
                .id(this.id)
                .car(this.car)
                .user(this.user)
                .rentalDate(this.rentalDate)
                .returnDate(this.returnDate)
                .status(status != null ? status : this.status)
                .build();
    }
}
