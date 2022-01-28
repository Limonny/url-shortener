package com.example.urlshortener.dao.url;

import com.example.urlshortener.model.UrlEntity;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UrlDAOImpl implements UrlDAO {

    private final JdbcTemplate template;

    @Override
    public List<UrlEntity> findAll(long limit, long offset) {
        String sql = "SELECT * FROM url ORDER BY short LIMIT ? OFFSET ?";

        return template.query(sql, new UrlEntityMapper(), limit, offset);
    }

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

    @Override
    public int deleteByShortUrl(String shortUrl) {
        String sql = "DELETE FROM url WHERE short = ?";

        return template.update(sql, shortUrl);
    }
}