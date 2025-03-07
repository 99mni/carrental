package org.example.carrental.car.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CarResponse {

    private Long id;
    private String name;
    private String brand;
    private String status;
    private List<String> tags;



}