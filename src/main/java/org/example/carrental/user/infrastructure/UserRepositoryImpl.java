package org.example.carrental.user.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.carrental.user.domain.User;
import org.example.carrental.user.infrastructure.entity.UserEntity;
import org.example.carrental.user.service.port.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User saveUser(User user){
        UserEntity savedEntity = userJpaRepository.save(UserEntity.builder().name(user.getName()).build());
        return User.builder().id(savedEntity.getId()).name(savedEntity.getName()).build();
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(entity -> User.builder().id(entity.getId()).name(entity.getName()).build())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(entity -> User.builder().id(entity.getId()).name(entity.getName()).build());
    }

    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

}
