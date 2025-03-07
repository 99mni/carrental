package org.example.carrental.testdouble;

import lombok.extern.slf4j.Slf4j;
import org.example.carrental.user.domain.User;
import org.example.carrental.user.service.port.UserRepository;

import java.util.*;

@Slf4j
public class FakeUserRepository implements UserRepository {

    Map<Long, User> users = new HashMap<>();
    long nextId = 1L;

    @Override
    public User saveUser(User user) {
        User savedUser = User.builder().id(nextId++).name(user.getName()).build();
        users.put(savedUser.getId(), savedUser);
        return savedUser;
    }

    @Override
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

}
