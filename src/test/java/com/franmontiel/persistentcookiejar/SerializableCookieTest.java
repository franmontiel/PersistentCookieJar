package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.persistence.SerializableCookie;

import org.junit.Test;

import okhttp3.Cookie;

import static org.junit.Assert.assertEquals;

public class SerializableCookieTest {

    @Test
    public void cookieSerialization() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        String serializedCookie = new SerializableCookie().encode(cookie);
        Cookie deserializedCookie = new SerializableCookie().decode(serializedCookie);

        assertEquals(cookie, deserializedCookie);
    }

    @Test
    public void hostOnlyDomainCookieSerialization() throws Exception {
        Cookie cookie = TestCookieCreator.createPersistentCookie(true);

        String serializedCookie = new SerializableCookie().encode(cookie);
        Cookie deserializedCookie = new SerializableCookie().decode(serializedCookie);

        assertEquals(cookie, deserializedCookie);
    }

    @Test
    public void nonPersistentCookieSerialization() throws Exception {
        Cookie cookie = TestCookieCreator.createNonPersistentCookie();

        String serializedCookie = new SerializableCookie().encode(cookie);
        Cookie deserializedCookie = new SerializableCookie().decode(serializedCookie);

        assertEquals(cookie, deserializedCookie);
    }


}