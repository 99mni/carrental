package org.example.carrental.car.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequest {
    private String name;
    private String brand;
    private String status;
    private List<String> tagNames;

    public CarRequest(String name, String brand) {
        this.name = name;
        this.brand = brand;
        this.status = "available";
    }

    public CarRequest(String name, String brand, String status) {
        this.name = name;
        this.brand = brand;
        this.status = status;
    }

}
