package com.example.authcrudpostgres.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AuthenticationException extends RuntimeException{
    public final static String INVALID_USERNAME = "auth.exception.invalidUsername";
    public final static String EXIST_USERNAME = "auth.exception.existUsername";
    public final static String EXIST_EMAIL = "auth.exception.existEmail";
    public final static String INVALID_ROLE = "auth.exception.invalidRole";


    Integer errorCode;
    HttpStatus httpStatus;
    public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException(String message, Integer errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }


}
