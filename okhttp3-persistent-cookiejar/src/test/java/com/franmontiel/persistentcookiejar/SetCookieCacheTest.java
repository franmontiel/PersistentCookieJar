package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import okhttp3.Cookie;

import static org.junit.Assert.assertEquals;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
public class SetCookieCacheTest {


    /**
     * Cookie equality used to update: same cookie-name, domain-value, and path-value.
     */
    @Test
    public void addAll_WithACookieEqualsToOneAlreadyStored_ShouldUpdateTheStoreCookie() {
        SetCookieCache cache = new SetCookieCache();
        cache.addAll(Collections.singleton(TestCookieCreator.createNonPersistentCookie("name", "first")));

        Cookie newCookie = TestCookieCreator.createNonPersistentCookie("name", "last");
        cache.addAll(Collections.singleton(newCookie));

        Cookie addedCookie = cache.iterator().next();
        assertEquals(newCookie, addedCookie);
    }

    /**
     * This is not RFC Compilant but strange things happen in the real world and it is intended to maintain a common behavior between Cache and Persistor
     * <p>
     * Cookie equality used to update: same cookie-name, domain-value, and path-value.
     */
    @Test
    public void addAll_WithMultipleEqualCookies_LastOneShouldRemain() {
        SetCookieCache cache = new SetCookieCache();
        Cookie equalCookieThatShouldNotBeAdded = TestCookieCreator.createPersistentCookie("name", "first");
        Cookie equalCookieThatShouldBeAdded = TestCookieCreator.createPersistentCookie("name", "last");

        cache.addAll(Arrays.asList(
                equalCookieThatShouldNotBeAdded,
                equalCookieThatShouldBeAdded));

        Cookie addedCookie = cache.iterator().next();
        assertEquals(equalCookieThatShouldBeAdded, addedCookie);
    }
}
