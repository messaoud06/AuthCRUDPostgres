package com.example.authcrudpostgres.controller;

import com.example.authcrudpostgres.model.PrayerTimes;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequestMapping("/api/public")
public class PrayerController {

    @GetMapping("/daily")
    PrayerTimes getDaily(@RequestParam String city){
        PrayerTimes prayerTimes = new PrayerTimes();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PrayerTimes> entity = new HttpEntity<>(headers);

        ResponseEntity<PrayerTimes> response = restTemplate.exchange( "https://muslimsalat.com/algiers/daily.json?key=960576e891ddb4e7ee926a4acb257916",
                HttpMethod.GET,
                entity,
                PrayerTimes.class
        );


        return response.getBody();
    }
}
