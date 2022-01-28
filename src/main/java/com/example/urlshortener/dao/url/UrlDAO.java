package com.example.urlshortener.dao.url;

import com.example.urlshortener.model.UrlEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlDAO {

    List<UrlEntity> findAll(long limit, long offset);

    Optional<UrlEntity> findByShortUrl(String shortUrl);

    Optional<UrlEntity> findByOriginalUrl(String originalUrl);

    void save(String shortUrl, String originalUrl);

    int deleteByShortUrl(String shortUrl);
}