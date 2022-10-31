package com.example.authcrudpostgres.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PrayerTimes {

    public String title;
    public String query;
    @JsonProperty("for")
    public String myfor;
    public int method;
    public String prayer_method_name;
    public String daylight;
    public String timezone;
    public String map_image;
    public String sealevel;
    public TodayWeather today_weather;
    public String link;
    public String qibla_direction;
    public String latitude;
    public String longitude;
    public String address;
    public String city;
    public String state;
    public String postal_code;
    public String country;
    public String country_code;
    public ArrayList<Item> items;
    public int status_valid;
    public int status_code;
    public String status_description;

}

@Getter
@Setter
class TodayWeather{
    public int pressure;
    public String temperature;
}

@Getter
@Setter
class Item{
    public String date_for;
    public String fajr;
    public String shurooq;
    public String dhuhr;
    public String asr;
    public String maghrib;
    public String isha;
}