package com.example.authcrudpostgres.config.security;

import com.example.authcrudpostgres.entity.User;
import com.example.authcrudpostgres.exception.AuthenticationException;
import com.example.authcrudpostgres.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {

            // Enable CORS and disable CSRF
            http = http.cors().and().csrf().disable();

            // Set session management to stateless
            http = http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and();

            // Set unauthorized requests exception handler
            http = http
                    .exceptionHandling()
                    .authenticationEntryPoint(
                            (request, response, ex) -> {
                                //log.warn("authenticationEntryPoint SC_UNAUTHORIZED");
                                ex.printStackTrace();
                                this.writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                            }
                    )
                    .and();
            // Set permissions on endpoints
            http.authorizeRequests()
                    // Our public endpoints
                    .antMatchers("/").permitAll()
                    .antMatchers("/auth/**").permitAll()
                    .antMatchers("/api/public/**").permitAll()
                    .antMatchers("/v3/**").permitAll()
                    // Our private endpoints
                    .antMatchers("/api/**").authenticated()
                    .anyRequest().authenticated();

            //Custom Authentication Manager
            http.authenticationManager(new CustomAuthenticationManager());

            // Add JWT token filter
            http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    private void writeResponse(HttpServletResponse httpResponse, int code, String message) throws IOException {
        httpResponse.setContentType("application/json");
        httpResponse.setStatus(code);
        httpResponse.getOutputStream().write(("{\"code\":" + code + ",").getBytes());
        httpResponse.getOutputStream().write(("\"message\":\"" + message + "\"}").getBytes());
        httpResponse.getOutputStream().flush();
    }
}


