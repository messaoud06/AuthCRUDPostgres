package com.example.authcrudpostgres.model;

import lombok.Data;

@Data
public class GenericResponse {
    public final static String SUCCESS = "Success";
    public final static String ERROR = "Error";
    public final static String ALREADY_USED = "already_Used";

    private int code;
    private String message;

    public GenericResponse(int code, String message) {
        super();
        this.message = message;
        this.code = code;
    }


}
