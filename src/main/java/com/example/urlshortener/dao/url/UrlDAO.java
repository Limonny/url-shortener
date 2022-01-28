package com.example.urlshortener.dao.url;

import com.example.urlshortener.model.UrlEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlDAO {

    List<UrlEntity> findAll(long limit, long offset);

    Optional<UrlEntity> findByShortUrl(String shortUrl);

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    void save(String shortUrl, String originalUrl, LocalDate expire);

    int deleteByShortUrl(String shortUrl);

    void updateExpiryDate(String shortUrl, LocalDate expire);

    void deleteExpired();
}