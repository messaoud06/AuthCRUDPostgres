package com.example.authcrudpostgres.repository;

import com.example.authcrudpostgres.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

    Boolean existsUserByUsername(String username);
    Boolean existsUserByEmail(String username);
}
