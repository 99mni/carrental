package org.example.carrental.user.service.port;

import org.example.carrental.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User saveUser(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}
