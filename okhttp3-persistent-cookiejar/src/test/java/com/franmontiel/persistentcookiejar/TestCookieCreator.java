package com.franmontiel.persistentcookiejar;

import okhttp3.Cookie;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
class TestCookieCreator {

    private TestCookieCreator() {
    }

    public static Cookie createPersistentCookie(boolean hostOnlyDomain) {
        Cookie.Builder builder = new Cookie.Builder()
                .path("/")
                .name("name")
                .value("value")
                .expiresAt(System.currentTimeMillis() + 24 * 60 * 60 * 1000)
                .httpOnly()
                .secure();
        if (hostOnlyDomain) {
            builder.hostOnlyDomain("domain.com");
        } else {
            builder.domain("domain.com");
        }
        return builder.build();
    }

    public static Cookie createPersistentCookie(String name, String value) {
        return new Cookie.Builder()
                .domain("domain.com")
                .path("/")
                .name(name)
                .value(value)
                .expiresAt(System.currentTimeMillis() + 24 * 60 * 60 * 1000)
                .httpOnly()
                .secure()
                .build();
    }

    public static Cookie createNonPersistentCookie() {
        return new Cookie.Builder()
                .domain("domain.com")
                .path("/")
                .name("name")
                .value("value")
                .httpOnly()
                .secure()
                .build();
    }

    public static Cookie createNonPersistentCookie(String name, String value) {
        return new Cookie.Builder()
                .domain("domain.com")
                .path("/")
                .name(name)
                .value(value)
                .httpOnly()
                .secure()
                .build();
    }


    public static Cookie createExpiredCookie() {
        return new Cookie.Builder()
                .domain("domain.com")
                .path("/")
                .name("name")
                .value("value")
                .expiresAt(Long.MIN_VALUE)
                .httpOnly()
                .secure()
                .build();
    }
}
