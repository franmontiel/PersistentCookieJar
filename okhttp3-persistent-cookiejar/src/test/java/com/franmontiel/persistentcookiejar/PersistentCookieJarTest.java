package com.franmontiel.persistentcookiejar;

import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Francisco J. Montiel on 11/02/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PersistentCookieJarTest {

    private PersistentCookieJar persistentCookieJar;

    private HttpUrl url = HttpUrl.parse("https://domain.com/");

    @Before
    public void createCookieJar() {
        persistentCookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(RuntimeEnvironment.application.getApplicationContext())
        );
    }

    /**
     * Test that the cookie is stored and also loaded when the a matching url is given
     */
    @Test
    public void regularCookie() {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);

        persistentCookieJar.saveFromResponse(url, Collections.singletonList(cookie));
        List<Cookie> storedCookies = persistentCookieJar.loadForRequest(url);

        assertEquals(cookie, storedCookies.get(0));
    }

    /**
     * Test that a stored cookie is not loaded for a non matching url.
     */
    @Test
    public void differentUrlRequest() {
        Cookie cookie = TestCookieCreator.createPersistentCookie(false);
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(cookie));

        List<Cookie> storedCookies = persistentCookieJar.loadForRequest(HttpUrl.parse("https://otherdomain.com"));

        assertTrue(storedCookies.isEmpty());
    }

    @Test
    /**
     * Test that when receiving a cookie equal(cookie-name, domain-value, and path-value) to one that is already stored then the old cookie is overwritten by the new one.
     */
    public void updateCookie() {
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createPersistentCookie("name", "first")));

        Cookie newCookie = TestCookieCreator.createPersistentCookie("name", "last");
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(newCookie));

        List<Cookie> storedCookies = persistentCookieJar.loadForRequest(url);
        assertTrue(storedCookies.size() == 1);
        assertEquals(newCookie, storedCookies.get(0));
    }

    /**
     * Test that a expired cookie is not retrieved
     */
    @Test
    public void expiredCookie() {
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createExpiredCookie()));

        List<Cookie> cookies = persistentCookieJar.loadForRequest(url);

        assertTrue(cookies.isEmpty());
    }

    /**
     * Test that when receiving an expired cookie equal(cookie-name, domain-value, and path-value) to one that is already stored then the old cookie is overwritten by the new one.
     */
    @Test
    public void removeCookieWithExpiredOne() {
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createPersistentCookie(false)));

        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createExpiredCookie()));

        assertTrue(persistentCookieJar.loadForRequest(url).isEmpty());
    }

    /**
     * Test that the session cookies are cleared without affecting to the persisted cookies
     */
    @Test
    public void clearSessionCookies() {
        Cookie persistentCookie = TestCookieCreator.createPersistentCookie(false);
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(persistentCookie));
        persistentCookieJar.saveFromResponse(url, Collections.singletonList(TestCookieCreator.createNonPersistentCookie()));

        persistentCookieJar.clearSession();

        assertTrue(persistentCookieJar.loadForRequest(url).size() == 1);
        assertEquals(persistentCookieJar.loadForRequest(url).get(0), persistentCookie);
    }

    @After
    public void cleanCookieJar() {
        persistentCookieJar.clear();
    }
}
