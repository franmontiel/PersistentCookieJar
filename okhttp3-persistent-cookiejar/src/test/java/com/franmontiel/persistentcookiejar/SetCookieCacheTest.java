package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import okhttp3.Cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
public class SetCookieCacheTest {

    @Test
    public void updateCookie() throws Exception {
        SetCookieCache cache = new SetCookieCache();

        cache.addAll(Collections.singleton(TestCookieCreator.createPersistentCookie(false)));
        cache.addAll(Collections.singleton(TestCookieCreator.createPersistentCookie(false)));

        Iterator<Cookie> iterator = cache.iterator();
        int size = 0;
        while (iterator.hasNext()) {
            iterator.next();
            size++;
        }

        assertTrue(size == 1);
    }

    @Test
    public void updateCookieWithDifferentValues() throws Exception {
        SetCookieCache cache = new SetCookieCache();

        cache.addAll(Arrays.asList(
                TestCookieCreator.createNonPersistentCookie("name", "first"),
                TestCookieCreator.createNonPersistentCookie("name", "last")
        ));

        Iterator<Cookie> iterator = cache.iterator();
        String value = null;
        while (iterator.hasNext()) {
            value = iterator.next().value();
        }

        assertEquals("last", value);
    }
}
