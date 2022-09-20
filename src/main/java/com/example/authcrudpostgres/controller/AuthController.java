package com.example.authcrudpostgres.controller;

import com.example.authcrudpostgres.config.security.CustomAuthenticationManager;
import com.example.authcrudpostgres.config.security.JwtTokenUtil;
import com.example.authcrudpostgres.config.security.UserDetailsImpl;
import com.example.authcrudpostgres.entity.Role;
import com.example.authcrudpostgres.entity.User;
import com.example.authcrudpostgres.exception.AuthenticationException;
import com.example.authcrudpostgres.model.GenericResponse;
import com.example.authcrudpostgres.model.LoginResponse;
import com.example.authcrudpostgres.model.RegisterRequest;
import com.example.authcrudpostgres.enumuration.Roles;
import com.example.authcrudpostgres.repository.RoleRepository;
import com.example.authcrudpostgres.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    CustomAuthenticationManager authenticationManager;


    @Autowired
    JwtTokenUtil jwtTokenUtil;


    @PostMapping("/register")
    public GenericResponse register(@Valid @RequestBody RegisterRequest registerRequest){

        if (userRepository.existsUserByUsername(registerRequest.getUsername())) {
            throw new AuthenticationException(AuthenticationException.EXIST_USERNAME, 1001 ,HttpStatus.UNAUTHORIZED);
        }

        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new AuthenticationException(AuthenticationException.EXIST_EMAIL, 1002 ,HttpStatus.UNAUTHORIZED);
        }

        //create User
        User user = new User(registerRequest.getUsername(),registerRequest.getEmail(), new BCryptPasswordEncoder().encode(registerRequest.getPassword()));

        Set<String> requestRoles = registerRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if(requestRoles == null || requestRoles.isEmpty()){
            requestRoles = new HashSet<>();
            requestRoles.add(Roles.ROLE_USER.toString());
        }

        if (registerRequest == null){
            Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
        else{
            requestRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN)
                                .orElseThrow(() ->  new AuthenticationException(AuthenticationException.INVALID_ROLE, 1003 ,HttpStatus.UNAUTHORIZED));
                        roles.add(adminRole);
                        break;

                    case "mod":
                        Role modRole = roleRepository.findByName(Roles.ROLE_MODERATOR)
                                .orElseThrow(() -> new AuthenticationException(AuthenticationException.INVALID_ROLE, 1003 ,HttpStatus.UNAUTHORIZED));
                        roles.add(modRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName(Roles.ROLE_USER)
                                .orElseThrow(() ->  new AuthenticationException(AuthenticationException.INVALID_ROLE, 1003 ,HttpStatus.UNAUTHORIZED));
                        roles.add(userRole);
                }

            });
        }

        user.setEnabled(1);
        user.setRoles(roles);
        userRepository.save(user);

            return new GenericResponse(0,"User registered successfully!");
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody User request ){

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetailsImpl userPrincipal = (UserDetailsImpl) authenticate.getPrincipal();

            return new LoginResponse(0, GenericResponse.SUCCESS,
                    jwtTokenUtil.generateAccessToken(userPrincipal.getUser()),
                    userPrincipal.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList()),
                    userPrincipal.getUsername());

        } catch (BadCredentialsException ex) {
            throw new AuthenticationException(ex.getMessage(), 2, HttpStatus.UNAUTHORIZED);
        } catch (DisabledException ex) {
            throw new AuthenticationException(ex.getMessage(), 3, HttpStatus.UNAUTHORIZED);
        } catch (AccountExpiredException ex) {
            throw new AuthenticationException(ex.getMessage(), 4, HttpStatus.UNAUTHORIZED);
        }
    }
}
