package com.example.authcrudpostgres.config.security;

import com.example.authcrudpostgres.entity.Role;
import com.example.authcrudpostgres.entity.User;
import com.example.authcrudpostgres.model.JwtValidationModel;
import com.example.authcrudpostgres.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("**** --- JwtTokenFilter --- ****");

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            this.writeResponse(response, HttpServletResponse.SC_OK, "Pre-flight Request accepted");
        }

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();

        JwtValidationModel jwtValidation = jwtTokenUtil.validate(token);

        if (!jwtValidation.isStatus()) {

            this.writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, jwtValidation.getMessage());
            return;
        }
        HttpServletRequest httpServletRequest = request;
        httpServletRequest.setAttribute("username", jwtTokenUtil.getUsername(token));
        httpServletRequest.setAttribute("roles", jwtTokenUtil.getRoles(token));

        // Get user identity and set it on the spring security context
        User user = userRepository.findByUsername(jwtTokenUtil.getUsername(token)).orElse(null);

        UserDetails userDetails = user == null ? null : UserDetailsImpl.build(user) ;

        // log access
        String method = request.getMethod();
        // Create a methodAndPath like [GET]/api/register-request
        String path = new UrlPathHelper().getPathWithinApplication(request);
        String methodAndPath = "[" + method + "]" + path;
        log.info("Requested URL {} by user {}", methodAndPath, jwtTokenUtil.getUsername(token));
        for(GrantedAuthority g:userDetails.getAuthorities()){
            System.out.println(g.getAuthority());
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                jwtTokenUtil.getUsername(token), null, userDetails == null ? null :  userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(httpServletRequest, response);
    }
    private void writeResponse(HttpServletResponse httpResponse, int code, String message) throws IOException {
        httpResponse.setContentType("application/json");
        httpResponse.setStatus(code);
        httpResponse.getOutputStream().write(("{\"code\":" + code + ",").getBytes());
        httpResponse.getOutputStream().write(("\"message\":\"" + message + "\"}").getBytes());
        httpResponse.getOutputStream().flush();
    }
}
