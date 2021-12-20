package com.franmontiel.persistentcookiejar;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
class TestCookieCreator {

    private static final String DEFAULT_DOMAIN = "domain.com";
    private static final String DEFAULT_PATH = "/";

    public static final HttpUrl DEFAULT_URL = HttpUrl.parse("https://" + DEFAULT_DOMAIN + DEFAULT_PATH);
    public static final HttpUrl OTHER_URL = HttpUrl.parse("https://otherdomain.com/");

    private TestCookieCreator() {
    }

    public static Cookie createPersistentCookie(boolean hostOnlyDomain) {
        Cookie.Builder builder = new Cookie.Builder()
                .path(DEFAULT_PATH)
                .name("name")
                .value("value")
                .expiresAt(System.currentTimeMillis() + 24 * 60 * 60 * 1000)
                .httpOnly()
                .secure();
        if (hostOnlyDomain) {
            builder.hostOnlyDomain(DEFAULT_DOMAIN);
        } else {
            builder.domain(DEFAULT_DOMAIN);
        }
        return builder.build();
    }

    public static Cookie createPersistentCookie(String name, String value) {
        return new Cookie.Builder()
                .domain(DEFAULT_DOMAIN)
                .path(DEFAULT_PATH)
                .name(name)
                .value(value)
                .expiresAt(System.currentTimeMillis() + 24 * 60 * 60 * 1000)
                .httpOnly()
                .secure()
                .build();
    }

    public static Cookie createNonPersistentCookie() {
        return new Cookie.Builder()
                .domain(DEFAULT_DOMAIN)
                .path(DEFAULT_PATH)
                .name("name")
                .value("value")
                .httpOnly()
                .secure()
                .build();
    }

    public static Cookie createNonPersistentCookie(String name, String value) {
        return new Cookie.Builder()
                .domain(DEFAULT_DOMAIN)
                .path(DEFAULT_PATH)
                .name(name)
                .value(value)
                .httpOnly()
                .secure()
                .build();
    }


    public static Cookie createExpiredCookie() {
        return new Cookie.Builder()
                .domain(DEFAULT_DOMAIN)
                .path(DEFAULT_PATH)
                .name("name")
                .value("value")
                .expiresAt(Long.MIN_VALUE)
                .httpOnly()
                .secure()
                .build();
    }
}
