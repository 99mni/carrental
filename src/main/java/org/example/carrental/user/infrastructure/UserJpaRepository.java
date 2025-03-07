package org.example.carrental.user.infrastructure;

import org.example.carrental.user.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

}
