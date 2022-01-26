package com.example.urlshortener.controller;


import com.example.urlshortener.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;
}