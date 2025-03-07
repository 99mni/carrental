package org.example.carrental.car.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.carrental.car.domain.Car;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CarEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "자동차 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "브랜드는 필수입니다.")
    private String brand;

    @Column(nullable = false)
    private String status = "available";

    @ManyToMany
    @JoinTable(
            name = "car_tag",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tags = new ArrayList<>();

    @PrePersist // 미리 기본값 설정
    public void prePersist() {
        if (this.status == null || this.status.isBlank()) {
            this.status = "available";
        }
    }

}