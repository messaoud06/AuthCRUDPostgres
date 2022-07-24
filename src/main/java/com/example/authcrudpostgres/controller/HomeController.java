package com.example.authcrudpostgres.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomeController {
    @GetMapping("/")
    public String home(){
        return "App Project API";
    }

}

