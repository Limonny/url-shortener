package com.example.urlshortener.controller;


import com.example.urlshortener.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;

    private final String domain = "localhost:8080/";

    @GetMapping("/{url}")
    public void redirectToOriginalUrl(@PathVariable String url,
                                      HttpServletResponse response) {
        String result = urlService.findOriginalUrl(url);

        response.setHeader("Location", result);
        response.setStatus(303);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> createShortUrl(@RequestParam String url) {
        String result = urlService.createShortUrl(url);

        return new ResponseEntity<>(domain + result, HttpStatus.CREATED);
    }
}