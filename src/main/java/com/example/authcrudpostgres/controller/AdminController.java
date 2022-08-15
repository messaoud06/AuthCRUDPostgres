package com.example.authcrudpostgres.controller;

import com.example.authcrudpostgres.entity.Role;
import com.example.authcrudpostgres.model.GenericResponse;
import com.example.authcrudpostgres.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/role")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminController {

    @Autowired
    RoleRepository roleRepository;


    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAll(){
        List<Role> roles =  roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable Long id){

        Optional<Role> role = roleRepository.findById(id);


        return role.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<Role> addRole(@Valid @RequestBody Role role) throws URISyntaxException {

        Role result = roleRepository.save(role);
        return ResponseEntity.created(new URI("/api/admin/role/"+ result.getId()))
                .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteRole(@PathVariable Long id){
        roleRepository.deleteById(id);
        return ResponseEntity.ok().body(new GenericResponse(0,GenericResponse.SUCCESS));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@Valid @RequestBody Role role,@PathVariable Long id) throws URISyntaxException {

        Role myRole = roleRepository.findById(id).orElse(null);

        if(myRole!=null){
            myRole.setName(role.getName());
            roleRepository.save(myRole);
            return ResponseEntity.ok().body(new GenericResponse(0,GenericResponse.SUCCESS));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
