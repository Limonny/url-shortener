package com.example.urlshortener.service;

import com.example.urlshortener.dao.url.UrlDAO;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.exception.UrlValidationException;
import com.example.urlshortener.model.UrlEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.example.urlshortener.util.ShortUrlGenerator.generateUrl;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UrlService {

    private final UrlDAO urlDAO;

    public List<UrlEntity> findAllWithLimit(Long limit, Long offset) {
        List<UrlEntity> urls = urlDAO.findAll(
                limit == null ? 100 : limit,
                offset == null ? 0 : offset);

        if (urls.isEmpty()) {
            throw new UrlNotFoundException(HttpStatus.NOT_FOUND,
                    "URLs not found");
        }

        return urls;
    }

    public UrlEntity findByShortUrl(String shortUrl) {
        return urlDAO.findByShortUrl(shortUrl).orElseThrow(() ->
                new UrlNotFoundException(HttpStatus.NOT_FOUND,
                        "Url entity with short URL: " + shortUrl + " does not exist"));
    }

    public String findOriginalUrl(String shortUrl) {
        UrlEntity url = urlDAO.findByShortUrl(shortUrl).orElseThrow(() ->
                new UrlNotFoundException(HttpStatus.NOT_FOUND,
                "The short URL: " + shortUrl + " does not exist"));

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

    @Transactional
    public void deleteByShortUrl(String shortUrl) {
        int modCount = urlDAO.deleteByShortUrl(shortUrl);

        if (modCount == 0) {
            throw new UrlNotFoundException(HttpStatus.NOT_FOUND,
                    "Short URL: " + shortUrl + " does not exist");
        }
    }
}