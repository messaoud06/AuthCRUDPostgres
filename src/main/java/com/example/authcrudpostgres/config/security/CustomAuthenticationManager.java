package com.example.authcrudpostgres.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserDetailsServiceImpl customUserDetailsService;

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UserDetailsImpl UserDetailsImpl = customUserDetailsService.loadUserByUsername(authentication.getName());
        if (!passwordEncoder().matches(authentication.getCredentials().toString(), UserDetailsImpl.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(UserDetailsImpl, UserDetailsImpl.getPassword(), UserDetailsImpl.getAuthorities());
    }

}