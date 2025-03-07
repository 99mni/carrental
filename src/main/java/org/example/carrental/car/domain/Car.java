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
public class Car {

    private Long id;
    private Long version;
    private String name;
    private String brand;
    private String status;

    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    public Car(Long id, String name, String brand, String status) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.status = status;
    }

    public void update(String name, String brand, String status) {
        this.name = name;
        this.brand = brand;
        this.status = status;
    }

}