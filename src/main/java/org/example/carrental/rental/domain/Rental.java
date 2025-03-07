package org.example.carrental.rental.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.carrental.car.domain.Car;
import org.example.carrental.user.domain.User;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    private Long id;
    private Car car;
    private User user;

    private LocalDate rentalDate;
    private LocalDate returnDate;
    private String status;

}
