package com.example.authcrudpostgres.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String id;
    private String author;
    private String title;
    private String genre;
    private String price;
    private String publish_date;
    private String description;

}
