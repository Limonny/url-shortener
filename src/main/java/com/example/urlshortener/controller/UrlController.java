package com.example.urlshortener.controller;


import com.example.urlshortener.security.SecurityUser;
import com.example.urlshortener.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@AllArgsConstructor
public class UrlController {

    private final UrlService urlService;

    private final String domain = "localhost:8080/";

    @GetMapping("/{url}")
    public void redirectToOriginalUrl(@PathVariable String url,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        String result = urlService.findOriginalUrl(url, request.getRemoteAddr());

        response.setHeader("Location", result);
        response.setStatus(303);
    }

    @GetMapping("/{url}/count")
    public ResponseEntity<Map<String, Long>> getVisitorsCountForShortUrl(@PathVariable String url) {
        Map<String, Long> result = urlService.getTotalVisitorsCount(url);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> createShortUrl(@RequestParam String url,
                                                 @RequestParam(required = false) Integer activeFor,
                                                 @AuthenticationPrincipal SecurityUser securityUser) {
        String result = urlService.createShortUrl(url, activeFor, securityUser != null);

        return new ResponseEntity<>(domain + result, HttpStatus.CREATED);
    }
}