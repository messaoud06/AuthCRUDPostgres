package com.example.authcrudpostgres.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString

public class InvalidArgumentResponse extends GenericResponse {
    List<FieldErrorModel> fieldsError;
    public InvalidArgumentResponse(int code, String message, List<FieldErrorModel> fieldsError){
        super(code, message);
        this.fieldsError = fieldsError;
    }
}
