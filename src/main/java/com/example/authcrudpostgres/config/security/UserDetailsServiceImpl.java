package com.example.authcrudpostgres.config.security;


import com.example.authcrudpostgres.entity.Role;
import com.example.authcrudpostgres.entity.User;
import com.example.authcrudpostgres.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    for (Role role : user.getRoles()){
      System.out.println(role.getName());
    }

    return UserDetailsImpl.build(user);
  }

}