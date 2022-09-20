package com.example.authcrudpostgres.config.security;

import com.example.authcrudpostgres.entity.User;
import com.example.authcrudpostgres.model.JwtValidationModel;
import com.example.authcrudpostgres.enumuration.Roles;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public String generateAccessToken(User user) {
        List<Roles> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
        Claims body = Jwts.claims()
                .setSubject(user.getUsername())
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION * 1000))
                ;
        body.put("roles", roles);

        return SecurityConstants.TOKEN_PREFIX + Jwts.builder()
                .setClaims(body)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
    }

    public List<String> getRoles(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles") != null ? (List<String>) claims.get("roles") : new ArrayList<>();
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }


    public JwtValidationModel validate(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return new JwtValidationModel(true, "Success");
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
            return new JwtValidationModel(false, "Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
            return new JwtValidationModel(false, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
            return new JwtValidationModel(false, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
            return new JwtValidationModel(false, "Unsupported JWT token ");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
            return new JwtValidationModel(false, "JWT claims string is empty");
        } catch (Exception ex) {
            log.error("JWT Exception  - {}", ex.getMessage());
            return new JwtValidationModel(false, "JWT others exceptions");
        }

    }



}
