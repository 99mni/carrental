package org.example.carrental.car.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    private Long id;
    private String name;

    @Builder.Default
    private List<Car> cars = new ArrayList<>();

}