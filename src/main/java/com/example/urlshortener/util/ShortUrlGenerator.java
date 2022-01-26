package com.example.urlshortener.util;

import java.util.concurrent.ThreadLocalRandom;

public class ShortUrlGenerator {

    private static final char[] SYMBOLS = new char[]
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
             'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
             'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
             'u', 'v', 'w', 'x', 'y', 'z'};

    private static final int URL_LENGTH = 6;

    public static String generateUrl() {
        StringBuilder builder = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < URL_LENGTH; i++) {
            builder.append(SYMBOLS[random.nextInt(SYMBOLS.length)]);
        }

        return builder.toString();
    }
}