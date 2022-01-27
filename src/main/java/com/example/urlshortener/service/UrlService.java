package com.example.urlshortener.service;

import com.example.urlshortener.dao.UrlDAO;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.exception.UrlValidationException;
import com.example.urlshortener.model.UrlEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

import static com.example.urlshortener.util.ShortUrlGenerator.generateUrl;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UrlService {

    private final UrlDAO urlDAO;

    public String findOriginalUrl(String shortUrl) {
        UrlEntity url = urlDAO.findByShortUrl(shortUrl).orElseThrow(() -> {
            throw new UrlNotFoundException(HttpStatus.NOT_FOUND,
                    "The short URL: " + shortUrl + " does not exist");
        });

        return url.getOriginalUrl();
    }

    @Transactional
    @Retryable(value = {SQLException.class})
    public String createShortUrl(String originalUrl) {
        if (!originalUrl.matches("(([\\w]+:)?//)?(([\\d\\w]|%[a-fA-f\\d]{2})+(:([\\d\\w]|%[a-fA-f\\d]{2})+)?@)?([\\d\\w][-\\d\\w]{0,253}[\\d\\w]\\.)+[\\w]{2,63}(:[\\d]+)?(/([-+_~.\\d\\w]|%[a-fA-f\\d]{2})*)*(\\?(&?([-+_~.\\d\\w]|%[a-fA-f\\d]{2})=?)*)?(#([-+_~.\\d\\w]|%[a-fA-f\\d]{2})*)?")) {
            throw new UrlValidationException(HttpStatus.BAD_REQUEST,
                    originalUrl + " - is not a valid URL");
        }

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        Optional<UrlEntity> o = urlDAO.findByOriginalUrl(originalUrl);
        if (o.isPresent()) {
            return o.get().getShortUrl();
        }

        String generatedUrl;
        do {
            generatedUrl = generateUrl();
        } while (urlDAO.findByShortUrl(generatedUrl).isPresent());

        urlDAO.save(generatedUrl, originalUrl);

        return generatedUrl;
    }
}