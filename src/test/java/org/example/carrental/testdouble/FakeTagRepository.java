package org.example.carrental.testdouble;

import org.example.carrental.car.domain.Tag;
import org.example.carrental.car.service.port.TagRepository;

import java.util.*;

public class FakeTagRepository implements TagRepository {

    Map<Long, Tag> tags = new HashMap<>();
    long nextId = 1L;

    @Override
    public Tag saveTag(Tag tag) {
        if (tag.getId() == null) {
            tag = Tag.builder().id(nextId++).name(tag.getName()).build();
        }
        tags.put(tag.getId(), tag);
        return tag;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return tags.values().stream()
                .filter(tag -> tag.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(tags.get(id));
    }

    @Override
    public List<Tag> findAll() {
        return new ArrayList<>(tags.values());
    }

    public void deleteById(Long id) {
        tags.remove(id);
    }
}
