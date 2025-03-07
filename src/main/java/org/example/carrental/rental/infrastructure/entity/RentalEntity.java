package org.example.carrental.rental.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.carrental.car.infrastructure.entity.CarEntity;
import org.example.carrental.user.infrastructure.entity.UserEntity;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    @NotNull(message = "자동차 정보는 필수")
    private CarEntity car;

    @ManyToOne
    @NotNull(message = "사용자 정보는 필수")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDate rentalDate;
    private LocalDate returnDate;
    private String status;


}
