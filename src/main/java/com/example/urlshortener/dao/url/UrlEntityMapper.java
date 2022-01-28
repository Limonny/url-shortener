package com.example.urlshortener.dao.url;

import com.example.urlshortener.model.UrlEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UrlEntityMapper implements RowMapper<UrlEntity> {

    @Override
    public UrlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UrlEntity url = new UrlEntity();

        url.setShortUrl(rs.getString("short"));
        url.setOriginalUrl(rs.getString("original"));
        url.setExpirationDate(rs.getDate("expire").toLocalDate());

        return url;
    }
}