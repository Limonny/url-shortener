package com.example.urlshortener.dao.visitor;

import org.springframework.stereotype.Repository;

@Repository
public interface VisitorDAO {

    void save(String shortUrl, String ipAddress);

    Long countVisitors(String shortUrl);

    Long countUniqueVisitors(String shortUrl);
}