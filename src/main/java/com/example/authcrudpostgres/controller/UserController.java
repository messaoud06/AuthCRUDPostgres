package com.example.authcrudpostgres.controller;

import com.example.authcrudpostgres.entity.User;
import com.example.authcrudpostgres.model.GenericResponse;
import com.example.authcrudpostgres.repository.UserRepository;
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
@RequestMapping("/api/admin/user")
@PreAuthorize("hasAnyRole('ADMIN')")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll(){
        List<User> users =  userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){

        Optional<User> user = userRepository.findById(id);

        return user.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse> deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().body(new GenericResponse(0,GenericResponse.SUCCESS));
    }


    @PostMapping("/")
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) throws URISyntaxException {

        User result = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/admin/user/"+ result.getId()))
                .body(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,@PathVariable Long id) throws URISyntaxException {

        User myUser = userRepository.findById(id).orElse(null);

        if(myUser!=null){
            myUser.setPassword(user.getPassword());
            myUser.setEmail(user.getEmail());
            myUser.setEnabled(user.getEnabled());
            myUser.setRoles(user.getRoles());
            userRepository.save(myUser);
            return ResponseEntity.ok().body(new GenericResponse(0,GenericResponse.SUCCESS));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
