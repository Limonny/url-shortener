package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlEntity;
import com.example.urlshortener.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/url")
@AllArgsConstructor
public class AdminUrlController {

    private final UrlService urlService;

    @GetMapping
    public ResponseEntity<List<UrlEntity>> getAll(@RequestParam(required = false) Long limit,
                                                  @RequestParam(required = false) Long offset) {
        List<UrlEntity> result = urlService.findAllWithLimit(limit, offset);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlEntity> getByShortUrl(@PathVariable String shortUrl) {
        UrlEntity result = urlService.findByShortUrl(shortUrl);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{shortUrl}")
    public void deleteByShortUrl(@PathVariable String shortUrl) {
        urlService.deleteByShortUrl(shortUrl);
    }
}