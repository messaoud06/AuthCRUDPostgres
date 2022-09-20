package com.example.authcrudpostgres.repository;

import com.example.authcrudpostgres.entity.Role;
import com.example.authcrudpostgres.enumuration.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles role);
}
