package com.example.authcrudpostgres.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Helper {

    public static String convertTimeTo24H(String time){

        time = time.toUpperCase(Locale.ROOT);
        if(time.length()==8 && (time.contains("AM") || time.contains("PM") )){
            String result =
                    LocalTime.parse(
                                    time,
                                    DateTimeFormatter.ofPattern(
                                            "hh:mm a" ,
                                            Locale.US
                                    )
                            )
                            .format( DateTimeFormatter.ofPattern("HH:mm") )
                    ;

            return result;
        }

        return time;
    }
}
