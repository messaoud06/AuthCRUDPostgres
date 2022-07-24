package com.example.authcrudpostgres.exception;

import com.example.authcrudpostgres.model.FieldErrorModel;
import com.example.authcrudpostgres.model.GenericResponse;
import com.example.authcrudpostgres.model.InvalidArgumentResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @Autowired
    MessageSource messageSource;

    private static final String FATAL_ERROR = "Une erreur inattendue s'est produite";
    private static final String PAGE_NOT_FOUND = "La ressource demandée n'existe pas";


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<GenericResponse> authException(HttpServletRequest request, AuthenticationException exception){
        log.warn("Authentication Exception {}", exception.getMessage());
        GenericResponse response = new GenericResponse(exception.getErrorCode(), messageSource.getMessage(exception.getMessage(), null, new Locale("fr")));
        return new ResponseEntity<>(response, exception.httpStatus);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected InvalidArgumentResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> fieldsErrorsMap = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .forEach(fieldError -> {
                    List<String> values = new ArrayList<>();
                    if (fieldsErrorsMap.containsKey(fieldError.getField())){
                        values = fieldsErrorsMap.get(fieldError.getField());
                    }
                    values.add(fieldError.getDefaultMessage());
                    //values.add(messageSource.getMessage(fieldError.getDefaultMessage(),null, new Locale("fr")));
                    fieldsErrorsMap.put(fieldError.getField(), values);
                });

        List<FieldErrorModel> errors = new ArrayList<>();
        fieldsErrorsMap.entrySet().stream().forEach(entry -> {
            errors.add(new FieldErrorModel(entry.getKey(), entry.getValue()));
        });
        InvalidArgumentResponse response = new InvalidArgumentResponse(400, "invalid Arguments", errors);
        log.error("Invalid Argument {}", errors.toString());
        return response;

    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<GenericResponse> authException(HttpServletRequest request, InternalAuthenticationServiceException exception){
        log.warn("InternalAuthenticationServiceException Exception {}", exception.getMessage());
        GenericResponse response = new GenericResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<GenericResponse> noHandlerFoundException(HttpServletRequest request, NoHandlerFoundException exception) {
        GenericResponse response = new GenericResponse(404, PAGE_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> globalException(HttpServletRequest request, Exception exception) {
        log.error("Global Exception {} {}",exception.getClass() ,exception.getMessage());
        GenericResponse response = new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
        exception.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
