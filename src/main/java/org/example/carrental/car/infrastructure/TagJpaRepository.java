package org.example.carrental.car.infrastructure;

import org.example.carrental.car.infrastructure.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);
}
