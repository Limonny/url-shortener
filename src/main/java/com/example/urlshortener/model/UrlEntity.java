package com.example.urlshortener.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlEntity {

    private String shortUrl;
    private String originalUrl;
}