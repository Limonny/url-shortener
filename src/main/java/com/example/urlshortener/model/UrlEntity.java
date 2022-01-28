package com.example.urlshortener.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UrlEntity {

    private String shortUrl;
    private String originalUrl;
    private LocalDate expirationDate;
}