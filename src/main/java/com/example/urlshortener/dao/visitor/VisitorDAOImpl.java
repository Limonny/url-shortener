package com.example.urlshortener.dao.visitor;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class VisitorDAOImpl implements VisitorDAO {

    private final JdbcTemplate template;

    @Override
    public void save(String shortUrl, String ipAddress) {
        String sql = "INSERT INTO visitor VALUES (null, ?, ?)";

        template.update(sql, shortUrl, ipAddress);
    }

    @Override
    public Long countVisitors(String shortUrl) {
        String sql = "SELECT COUNT(ip_address) FROM visitor WHERE url = ?";

        return template.queryForObject(sql, Long.class, shortUrl);
    }

    @Override
    public Long countUniqueVisitors(String shortUrl) {
        String sql = "SELECT COUNT(DISTINCT ip_address) FROM visitor WHERE url = ?";

        return template.queryForObject(sql, Long.class, shortUrl);
    }
}