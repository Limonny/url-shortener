package com.example.urlshortener.dao;

import com.example.urlshortener.model.UrlEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlDAO {

    Optional<UrlEntity> findByShortUrl(String shortUrl);

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    void save(String shortUrl, String originalUrl);
}