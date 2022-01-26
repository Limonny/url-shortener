package com.example.urlshortener.service;

import com.example.urlshortener.dao.UrlDAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UrlService {

    private final UrlDAO urlDAO;
}