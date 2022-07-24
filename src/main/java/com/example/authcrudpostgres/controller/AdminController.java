package com.example.authcrudpostgres.controller;

import com.example.authcrudpostgres.entity.Role;
import com.example.authcrudpostgres.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/role")
public class AdminController {

    @Autowired
    RoleRepository roleRepository;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAll(){
        List<Role> roles =  roleRepository.findAll();

        return ResponseEntity.ok(roles);
    }
}
