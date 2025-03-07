package org.example.carrental.car.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.carrental.car.domain.Tag;
import org.example.carrental.car.infrastructure.entity.TagEntity;
import org.example.carrental.car.service.port.TagRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        TagEntity savedEntity = tagJpaRepository.save(TagEntity.builder().id(tag.getId()).name(tag.getName()).build());
        return Tag.builder().id(savedEntity.getId()).name(savedEntity.getName()).build();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return tagJpaRepository.findById(id)
                .map(entity -> Tag.builder().id(entity.getId()).name(entity.getName()).build());
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return tagJpaRepository.findByName(name).map(entity -> Tag.builder().id(entity.getId()).name(entity.getName()).build());
    }

    @Override
    public List<Tag> findAll() {
        return tagJpaRepository.findAll().stream().map(entity -> Tag.builder().id(entity.getId()).name(entity.getName()).build()).collect(Collectors.toList());
    }

}
