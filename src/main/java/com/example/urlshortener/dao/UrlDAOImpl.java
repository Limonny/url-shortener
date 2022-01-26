package com.example.urlshortener.dao;

import com.example.urlshortener.model.UrlEntity;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UrlDAOImpl implements UrlDAO {

    private final JdbcTemplate template;

    @Override
    public Optional<UrlEntity> findByShortUrl(String shortUrl) {
        String sql = "SELECT * FROM url WHERE short = ?";

        return template.query(sql, new UrlEntityMapper(), shortUrl)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<UrlEntity> findByOriginalUrl(String originalUrl) {
        String sql = "SELECT * FROM url WHERE original = ?";

        return template.query(sql, new UrlEntityMapper(), originalUrl)
                .stream()
                .findFirst();
    }

    @Override
    public void save(String shortUrl, String originalUrl) {
        String sql = "INSERT INTO url VALUES (?, ?)";

        template.update(sql, shortUrl, originalUrl);
    }
}