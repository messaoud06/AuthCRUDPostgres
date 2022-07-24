package com.example.authcrudpostgres.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse extends GenericResponse {
    private String authorization;
    private String username;
    private List<String> roles;

    public LoginResponse(int code, String message, String authorization, List<String> roles, String username) {
        super(code, message);
        this.authorization = authorization;
        this.roles = roles;
        this.username = username;

    }

}