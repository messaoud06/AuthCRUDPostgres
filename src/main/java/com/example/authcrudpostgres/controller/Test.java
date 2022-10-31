package com.example.authcrudpostgres.controller;

import com.example.authcrudpostgres.util.Helper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Test {

    public static void main(String[] args) {

        //Helper helper = new Helper();


        System.out.println(Helper.convertTimeTo24H("03:30 pm"));
        System.out.println(Helper.convertTimeTo24H("05:30 pm"));
        System.out.println(Helper.convertTimeTo24H("11:30 pm"));
    }
}
