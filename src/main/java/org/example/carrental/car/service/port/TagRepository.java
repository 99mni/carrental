package org.example.carrental.car.service.port;

import org.example.carrental.car.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag saveTag(Tag tag);
    Optional<Tag> findByName(String name);
    Optional<Tag> findById(Long id);

    List<Tag> findAll();

}
