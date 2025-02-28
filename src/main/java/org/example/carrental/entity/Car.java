package org.example.carrental.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Car {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "name")
    @NotBlank(message = "자동차 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "브랜드는 필수입니다.")
    private String brand;

    @Column(nullable = false)
    private String status = "available";

    @PrePersist // 미리 기본값 설정
    public void prePersist() {
        if (this.status == null || this.status.isBlank()) {
            this.status = "available";
        }
    }

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference //순환참조 방지
    private List<Rental> rentals = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarTag> carTags = new ArrayList<>();


    public Car(String name, String brand, String status) {
        this.name = name;
        this.brand = brand;
        this.status = status;
    }

    public Car update(String name, String brand, String status) {
        this.name = name;
        this.brand = brand;
        this.status = status;
        return this;
    }




}
