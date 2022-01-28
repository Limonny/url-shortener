package com.example.urlshortener.service;

import com.example.urlshortener.dao.url.UrlDAO;
import com.example.urlshortener.dao.visitor.VisitorDAO;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.exception.UrlValidationException;
import com.example.urlshortener.model.UrlEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.urlshortener.util.ShortUrlGenerator.generateUrl;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UrlService {

    private final UrlDAO urlDAO;
    private final VisitorDAO visitorDAO;

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
                        "Url entity with short URL: " + shortUrl + " does not exist or has expired"));
    }

    @Transactional
    public String findOriginalUrl(String shortUrl, String ipAddress) {
        UrlEntity url = urlDAO.findByShortUrl(shortUrl).orElseThrow(() ->
                new UrlNotFoundException(HttpStatus.NOT_FOUND,
                "Short URL: " + shortUrl + " does not exist or has expired"));

        visitorDAO.save(shortUrl, ipAddress);

        return url.getOriginalUrl();
    }

    @Transactional
    @Retryable(value = {SQLException.class})
    public String createShortUrl(String originalUrl, Integer activeFor, boolean authenticated) {
        if (!originalUrl.matches("(([\\w]+:)?//)?(([\\d\\w]|%[a-fA-f\\d]{2})+(:([\\d\\w]|%[a-fA-f\\d]{2})+)?@)?([\\d\\w][-\\d\\w]{0,253}[\\d\\w]\\.)+[\\w]{2,63}(:[\\d]+)?(/([-+_~.\\d\\w]|%[a-fA-f\\d]{2})*)*(\\?(&?([-+_~.\\d\\w]|%[a-fA-f\\d]{2})=?)*)?(#([-+_~.\\d\\w]|%[a-fA-f\\d]{2})*)?")) {
            throw new UrlValidationException(HttpStatus.BAD_REQUEST,
                    originalUrl + " - is not a valid URL");
        }

        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            originalUrl = "https://" + originalUrl;
        }

        LocalDate today = LocalDate.now();
        Optional<UrlEntity> o = urlDAO.findByOriginalUrl(originalUrl);
        if (o.isPresent()) {
            String shortUrl = o.get().getShortUrl();
            LocalDate expire = o.get().getExpirationDate();
            if (authenticated && activeFor != null) {
                int months = activeFor >= 1 && activeFor <= 12 ? activeFor : 1;
                if (expire.isBefore(today.plusMonths(months))) {
                    urlDAO.updateExpiryDate(shortUrl, today.plusMonths(months));
                }
            }
            else {
                if (expire.isBefore(today.plusMonths(1))) {
                    urlDAO.updateExpiryDate(shortUrl, today.plusMonths(1));
                }
            }
            return shortUrl;
        }

        String generatedUrl;
        do {
            generatedUrl = generateUrl();
        } while (urlDAO.findByShortUrl(generatedUrl).isPresent());

        urlDAO.save(generatedUrl, originalUrl, today.plusMonths(1));

        return generatedUrl;
    }

    @Transactional
    public void deleteByShortUrl(String shortUrl) {
        int modCount = urlDAO.deleteByShortUrl(shortUrl);

        if (modCount == 0) {
            throw new UrlNotFoundException(HttpStatus.NOT_FOUND,
                    "Short URL: " + shortUrl + " does not exist or has expired");
        }
    }

    @Transactional
    public void prolongExpirationDate(String shortUrl, Integer days) {
        UrlEntity url = urlDAO.findByShortUrl(shortUrl).orElseThrow(() ->
                new UrlNotFoundException(HttpStatus.NOT_FOUND,
                        "Short URL: " + shortUrl + " does not exist or has expired"));

        urlDAO.updateExpiryDate(shortUrl, url.getExpirationDate().plusDays(days));
    }

    public Map<String, Long> getTotalVisitorsCount(String shortUrl) {
        urlDAO.findByShortUrl(shortUrl).orElseThrow(() ->
                new UrlNotFoundException(HttpStatus.NOT_FOUND,
                        "Short URL: " + shortUrl + " does not exist or has expired"));

        Map<String, Long> map = new HashMap<>();
        map.put("total", visitorDAO.countVisitors(shortUrl));
        map.put("unique", visitorDAO.countUniqueVisitors(shortUrl));

        return map;
    }

    @Transactional
    @Scheduled(timeUnit = TimeUnit.HOURS, initialDelay = 24, fixedRate = 24)
    protected void deleteExpiredUrls() {
        urlDAO.deleteExpired();
    }
}